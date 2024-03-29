package com.zerotohero.admin.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zerotohero.admin.utils.FileUploadUtil;
import com.zerotohero.admin.utils.UserCsvExporter;
import com.zerotohero.entities.Role;
import com.zerotohero.entities.User;

@Controller
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private UserCsvExporter exporter;
    
    private String page;
    private String sortField;
    private String sortDir;
    private String keyword;

    private String getPage() {
		return page;
	}
	private void setPage(String page) {
		this.page = page;
	}
    private String getSortField() {
        return sortField;
    }
    private void setSortField(String sortField) {
        this.sortField = sortField;
    }
    private String getSortDir() {
        return sortDir;
    }
    private void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }
    private String getKeyword() {
        return keyword;
    }
    private void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @GetMapping("/users")
    public String listFirstPage(Model model){
        setPage(String.valueOf(1));
        return listByPage(1, model,"email","asc", null);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model m, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
    	setPage(String.valueOf(pageNum));
    	setKeyword(keyword);
        setSortDir(sortDir);
        setSortField(sortField);

    	Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
    	List<User> users = page.getContent();
    	
    	long startCount = (pageNum - 1)*UserService.USERS_PER_PAGES+1;
    	long endCount = startCount+UserService.USERS_PER_PAGES-1;
    	if(endCount > page.getTotalElements()) {
    		endCount=page.getTotalElements();
    	}
    	
    	String reversSortDir = sortDir.equals("asc") ? "desc" : "asc";
    	int totalPages = (int) Math.ceil((double)page.getTotalElements()/(double)UserService.USERS_PER_PAGES);
    	
    	m.addAttribute("totalPages", totalPages);
    	m.addAttribute("currentPage",pageNum);
    	m.addAttribute("startCount",startCount);
    	m.addAttribute("endCount",endCount);
    	m.addAttribute("totalItems", page.getTotalElements());
    	m.addAttribute("listUsers", users);
    	m.addAttribute("sortField", sortField);
    	m.addAttribute("sortDir", sortDir);
    	m.addAttribute("reversSortDir", reversSortDir);
        m.addAttribute("keyword", keyword);

    	return "users";
    }
    
    @GetMapping("/users/new")
    public String newUser(Model model){
        List<Role> roleList = service.listRoles();
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roleList", roleList);
        model.addAttribute("pageTitle", "Create New User");
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = service.saveUser(user);

            String uploadDir = "user-photo/"+ savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName, multipartFile);
        }else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            service.saveUser(user);
        }

        System.out.println(user);


        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
        
        String firstPartOfEmail = user.getEmail().split("@")[0];
        
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword="+firstPartOfEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model){
        try {
            User user = service.get(id);
            List<Role> roleList = service.listRoles();
            model.addAttribute("user",user);
            model.addAttribute("pageTitle", "Edit User ID: "+id);
            model.addAttribute("roleList",roleList);

            return "user_form";
        }catch (UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model){
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message", "The User ID: "+id+" has been deleted successfully");
        }catch (UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes attributes){
        service.updateUserEnabledStatus(id,enabled);
        String page = getPage();
        String keyword = getKeyword();
        String sortDir = getSortDir();
        String sortField = getSortField();

        String status = enabled ? "Enabled" : "Disabled";
        String message = "The User ID: "+id+" Has Been "+status;

        attributes.addFlashAttribute("message",message);

        return page.equals("1") ? "redirect:/users" : "redirect:/users/page/"+page+"?sortField="+sortField+"&sortDir="+sortDir;
    }

    @GetMapping("/users/export/csv")
    public void exportToCsv(HttpServletResponse resp) throws IOException {
    	List<User> users = service.listAll();
    	exporter.export(users, resp);
    }
}