package br.com.fiap.api.repository;

import br.com.fiap.api.model.ConsentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConsentLogRepository extends JpaRepository<ConsentLog, Long> {
    List<ConsentLog> findByUsername(String username);
}
