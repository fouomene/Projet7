package com.oc.microservice.dao;

import com.oc.microservice.enums.Role;
import com.oc.microservice.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class UserDaoTest {

    @Autowired UserDao userRepository;

    @Test
    @DisplayName("Save new user when name is empty or null")
    void should_save_user_when_name_is_empty() {
        // given
        String uname = "toto_1";
        User user = new User();
        user.setUsername(uname);
        user.setMail("toto_1@oc.com");
        user.setPassword("toto_1-pwd");
        user.setFirstname("fname-toto_1");
        user.setName("");
        user.setRole(Role.NOT_CONNECTED);
        // when

        // then

        assertThatThrownBy(() -> {
            userRepository.save(user);
        }).isInstanceOf(ConstraintViolationException.class) //...nous verifions que l'enregistrement leve l'exception ConstraintViolationException
        .hasMessageContaining("Le nom doit être compris entre 3 et 100 caractères");//.. on check si le message de l'exception contient les passage ${o}
    }

    @Test
    @DisplayName("Save new userl")
    void should_save_user() {
        // given
        String uname = "toto_2";
        User user = new User();
        user.setUsername(uname);
        user.setMail("toto_2@oc.com");
        user.setPassword("toto_2-pwd");
        user.setFirstname("fname-toto_2");
        user.setName("toto_2");
        user.setRole(Role.NOT_CONNECTED);
        // when

        // then
        User saved = userRepository.save(user);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo(user.getName());
        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Find user by username")
    void should_find_user_by_username() {
        // given
        String uname = "toto@toto@toto";
        User user = new User();
        user.setUsername(uname);
        user.setMail("toto@oc.com");
        user.setPassword("toto-pwd");
        user.setFirstname("fname-toto");
        user.setName("TOTO");
        user.setRole(Role.USER);
        // when
        //...nous enregistrons un user
        userRepository.save(user);
        // then
        //.. puis nous essayons de recuperer l'utilisateur précédemment enregistré
        User found = userRepository.findUserByUsername(uname);
        assertThat(found).isNotNull();
        assertThat(found.getMail()).isEqualTo(user.getMail());
        assertThat(found.getName()).isEqualTo(user.getName());
    }

    @Test
    @DisplayName("Find all user")
    void shoud_find_all_user() {
        // given

        List<User> users = new ArrayList<>();
        for (int i=0;i<10;i++) {
            User user = new User();
            user.setUsername("toto-"+i);
            user.setMail("toto"+i+"@oc.com");
            user.setPassword("toto"+i+"-pwd");
            user.setFirstname("fname-toto-"+i);
            user.setName("TOTO-"+1);
            user.setRole(Role.values()[i%2]);
            users.add(user);
        }

        // when

        userRepository.saveAll(users);

        // then .. Dans cette session nous allons recuperer tous les utilisateurs deja enregistré
        List<User> all = userRepository.findAll();
        assertThat(all).isNotNull();
        assertThat(all).hasSizeGreaterThanOrEqualTo(10);

    }
}