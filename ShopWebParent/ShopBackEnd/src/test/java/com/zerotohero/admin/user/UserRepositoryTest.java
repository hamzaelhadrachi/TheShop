package com.zerotohero.admin.user;

import com.zerotohero.entities.Role;
import com.zerotohero.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

/**
 *  user repository test
 *  TODO add rest of unit test cases
 */
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
     void testCreateUserWithOneRole(){

        Role admin = entityManager.find(Role.class, 1);
        User user = repository.getUserByEmail("test.email@mail.com");

        if(user == null){
            User createUser = new User("test.email@mail.com","123", "testFirstName", "testLastName");
            createUser.addRole(admin);

            User saveUser = repository.save(createUser);
            assertThat(saveUser.getId()).isPositive();
        }


    }

    @Test
    void testListAllUsers(){
        Iterable<User> usersList = repository.findAll();
        usersList.forEach(System.out::println);
    }

    @Test
    void testGetUserByEmail(){
        User user = repository.getUserByEmail("test.email@mail.com");
        assertThat(user).isNotNull();
    }

    @Test
    void testUpdateUserDetails(){
        User usr = repository.findById(1).get();
        usr.setEnabled(true);
        usr.setEmail("new.updatedmail@gmail.com");

        repository.save(usr);
    }

    @Test
    public void testCountById(){
        Integer id = 1;
        Long countById = repository.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0L);
    }

    @Test
    public void testDisableUser(){
        Integer id = 1;
        repository.updateEnabledStatus(id,false);
    }

    @Test
    public void testEnableUser(){
        Integer id = 3;
        repository.updateEnabledStatus(id,true);
    }
    
    @Test
    public void testListFirstPage() {
    	int pageNumber = 0;
    	int pageSize = 4;
    	
    	Pageable pageable = PageRequest.of(pageNumber, pageSize);
    	Page<User> page = repository.findAll(pageable);
    	
    	List<User> users = page.getContent();
    	users.forEach(u -> System.out.println(u));
    	
    	assertThat(users.size()).isEqualTo(pageSize);
    	
    }

    @Test
    public void testSearchUiser() {
        String keyword = "bruce";
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repository.findAll(keyword, pageable);

        List<User> users = page.getContent();
        users.forEach(u -> System.out.println(u));

        assertThat(users.size()).isGreaterThan(0);
    }
}
