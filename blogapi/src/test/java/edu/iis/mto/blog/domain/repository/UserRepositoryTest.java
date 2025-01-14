package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        assertThat(users, Matchers.hasSize(1));
        assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldFindUserWithGivenFullFirstNameIgnoringCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("jan", "other", "other");
        assertThat(users.contains(user), is(equalTo(true)));
    }

    @Test
    public void shouldFindUserWithGivenFullLastNameIgnoringCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("other", "kowalski",
                "other");
        assertThat(users.contains(user), is(equalTo(true)));
    }

    @Test
    public void shouldFindUserWithGivenPartOfFirstNameIgnoringCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("ja", "other", "other");
        assertThat(users.contains(user), is(equalTo(true)));
    }

    @Test
    public void shouldFindUserWithGivenPartOfLastNameIgnoringCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("other", "alski",
                "other");
        assertThat(users.contains(user), is(equalTo(true)));
    }

    @Test
    public void shouldFindUserWithGivenFullMailIgnoringCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("other", "other",
                "john@domain.com");
        assertThat(users.contains(user), is(equalTo(true)));
    }

    @Test
    public void shouldNotFindUserWithGivenWrongMail() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("other", "other",
                "mail@mail.com");
        assertThat(users.contains(user), is(equalTo(false)));
    }

    @Test
    public void shouldNotFindUserWithGivenWrongFirstName() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("firstname", "other",
                "other");
        assertThat(users.contains(user), is(equalTo(false)));
    }

    @Test
    public void shouldNotFindUserWithGivenWrongLastName() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("other", "lastname",
                "other");
        assertThat(users.contains(user), is(equalTo(false)));
    }
}
