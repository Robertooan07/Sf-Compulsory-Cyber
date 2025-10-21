@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConsentTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ConsentLogRepository consentLogRepository;

    @Test
    public void testConsentLogging() {
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/consent?username=admin&policyVersion=v1.0", null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<ConsentLog> logs = consentLogRepository.findByUsername("admin");
        assertFalse(logs.isEmpty());
    }
}
