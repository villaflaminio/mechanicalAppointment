package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.model.Role;
import it.mtempobono.mechanicalappointment.model.dto.RoleDto;
import it.mtempobono.mechanicalappointment.repository.RoleRepository;
import it.mtempobono.mechanicalappointment.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service per gestione role
 */
@Slf4j
@Service
@Transactional
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    // ===========================================================================
    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    // ===========================================================================

    /**
     * @param roleDto role da salvare
     * @return role salvato
     */
    public Role save(RoleDto roleDto) {
        try {
            // Controllo se il seriale esiste esiste già
            Role role = roleRepository.findById(roleDto.getId()).orElse(null);

            if (role == null){
                role = new Role();

                // Copio i dati
                BeanUtils.copyProperties(roleDto, role);

                role.setId(null);

                // Salvo role
                return roleRepository.save(role);
            }else{
                log.error("Role con seriale già esistente");
                throw new Exception("Role con seriale già esistente");
            }

        } catch (Exception e) {
            log.error("errore salvataggio role", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param id identificativo di role da eliminare
     */
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    /**
     * @param id identificativo di role da cercare
     * @return role cercato
     */
    public Role findById(Long id) {
        // Controllo se role esiste e lo restituisco, altrimenti lancio eccezione di not found
        return roleRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * @return lista di tutti gli roles
     */
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    /**
     * @param roleDto role modificato
     * @param id             identificativo di role da modificare
     * @return role modificato
     */
    public Role update(RoleDto roleDto, Long id) {
        // Ottengo role da modificare
        Optional<Role> roleOld = roleRepository.findById(id);
        roleDto.setId(id);

        // Controllo se role esiste
        if (roleOld.isPresent()) {
            // Copio i dati
            copyNonNullProperties(roleDto, roleOld.get());
            roleDto.setId(id);

            // Salvo role
            return roleRepository.save(roleOld.get());
        } else {
            // Se non esiste lancio eccezione di not found
            throw new ResourceNotFoundException();
        }
    }

    /**
     * @param probe         role utilizzato per filtrare
     * @param page          pagina da visualizzare
     * @param size          numero di elementi per pagina
     * @param sortField     campo per ordinamento
     * @param sortDirection direzione di ordinamento
     * @return lista di roles filtrati
     */
    public ResponseEntity<Page<Role>> filter(Role probe, Integer page, Integer size, String sortField, String sortDirection) {
        Pageable pageable;

        // Controllo se role da filtrare è nullo.
        if (probe == null) {
            probe = new Role(); // Se è nullo creo un nuovo role
        }

        // Controllo se il campo di ordinamento è nullo.
        if (StringUtils.isEmpty(sortField)) {
            pageable = PageRequest.of(page, size); // Se è nullo ordino per id
        } else {
            // Se non è nullo ordino per il campo di ordinamento
            Sort.Direction dir = StringUtils.isEmpty(sortDirection) ? Sort.Direction.ASC : Sort.Direction.valueOf(sortDirection.trim().toUpperCase());
            pageable = PageRequest.of(page, size, dir, sortField);
        }

        // Filtro gli roles
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
        Example<Role> example = Example.of(probe, matcher);

        return ResponseEntity.ok(roleRepository.findAll(example, pageable));
    }
}