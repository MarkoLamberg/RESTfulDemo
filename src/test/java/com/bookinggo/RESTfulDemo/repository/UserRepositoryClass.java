/*package com.bookinggo.RESTfulDemo.repository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @Test
    public void testFindByUsername() {
        Optional<User> user = repository.findByUsername("admin");
        assertTrue(user.isPresent());

        user = repository.findByUsername("nobody");
        assertFalse(user.isPresent());
    }
}*/
