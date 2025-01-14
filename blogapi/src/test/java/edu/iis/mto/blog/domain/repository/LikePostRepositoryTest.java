package edu.iis.mto.blog.domain.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest

public class LikePostRepositoryTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private LikePostRepository likePostRepository;

        private User user;
        private LikePost likePost;
        private BlogPost blogPost;

        @Before
        public void setUp() {
            user = new User();
            user.setFirstName("Jan");
            user.setLastName("Kowalski");
            user.setEmail("john@domain.com");
            user.setAccountStatus(AccountStatus.NEW);
            userRepository.save(user);

            blogPost = new BlogPost();
            blogPost.setEntry("test post");
            blogPost.setUser(user);
            entityManager.persist(blogPost);

            likePost = new LikePost();
            likePost.setPost(blogPost);
            likePost.setUser(user);
        }

    @Test
    public void shouldFindNoLikesIfLikesRepositoryIsEmpty() {
        List<LikePost> likedPosts = likePostRepository.findAll();
        assertThat(likedPosts, hasSize(0));
    }

    @Test
    public void shouldFindNoLikesOfUserIfLikesRepositoryIsEmptyUsingFindUserAndPost() {
        Optional<LikePost> likedPosts = likePostRepository.findByUserAndPost(user, blogPost);
        Assert.assertThat(likedPosts, is(Optional.empty()));
    }

    @Test
    public void shouldFindOneLikeIfLikesRepositoryHasOneLikePost() {
        likePostRepository.save(likePost);
        List<LikePost> likedPosts = likePostRepository.findAll();
        assertThat(likedPosts, hasSize(1));
        assertThat(likedPosts.get(0), is(equalTo(likePost)));
    }

    @Test
    public void shouldFindOneLikeOfExactUserIfLikesRepositoryHasOneLikePostUsingFindUserAndPost() {
        likePostRepository.save(likePost);
        Optional<LikePost> optional = likePostRepository.findByUserAndPost(user, blogPost);
        List<LikePost> likedPosts = optional.map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
        assertThat(likedPosts, hasSize(1));
        assertThat(likedPosts.get(0), is(equalTo(likePost)));
    }

    @Test
    public void shouldUpdateDataOfLikeInDatabase() {
        likePostRepository.save(likePost);
        List<LikePost> likedPosts = likePostRepository.findAll();
        LikePost temp = likedPosts.get(0);
        temp.getPost()
                .setEntry("new entry");
        likePostRepository.save(temp);
        likedPosts = likePostRepository.findAll();
        assertThat(likedPosts.get(0)
                        .getPost()
                        .getEntry(),
                is(equalTo("new entry")));

    }
}
