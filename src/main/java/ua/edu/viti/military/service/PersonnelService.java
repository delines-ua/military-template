package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.PersonnelCreateDTO;
import ua.edu.viti.military.dto.request.PersonnelUpdateDTO;
import ua.edu.viti.military.dto.response.PersonnelResponseDTO;
import ua.edu.viti.military.entity.MilitaryUnit;
import ua.edu.viti.military.entity.Personnel;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.mapper.PersonnelMapper;
import ua.edu.viti.military.repository.MilitaryUnitRepository;
import ua.edu.viti.military.repository.PersonnelRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PersonnelService {

    private final PersonnelRepository personnelRepository;
    private final MilitaryUnitRepository militaryUnitRepository;
    private final PersonnelMapper personnelMapper;

    @Transactional
    // При створенні нового запису бажано очистити кеш списків (якщо ви кешуєте getAll),
    // але для одиночного getById це не критично.
    public PersonnelResponseDTO create(PersonnelCreateDTO dto) {
        if (personnelRepository.existsByMilitaryId(dto.getMilitaryId())) {
            throw new RuntimeException("Військовий з таким ID вже існує");
        }

        Personnel personnel = personnelMapper.toEntity(dto);

        if (dto.getUnitId() != null) {
            MilitaryUnit unit = militaryUnitRepository.findById(dto.getUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Підрозділ не знайдено"));
            personnel.setUnit(unit);
        }

        return personnelMapper.toDTO(personnelRepository.save(personnel));
    }

    // --- ТУТ ВСТАВЛЯЄМО @Cacheable ---
    // Redis перевірить, чи є ключ "personnel::1". Якщо є - поверне його.
    // Якщо ні - виконає метод і збереже результат.
    @Cacheable(value = "personnel", key = "#id")
    public PersonnelResponseDTO getById(Long id) {
        log.info("Fetching personnel from DB: {}", id); // Цей лог буде тільки якщо даних немає в кеші
        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Не знайдено"));
        return personnelMapper.toDTO(personnel);
    }

    public List<PersonnelResponseDTO> getAll() {
        return personnelMapper.toDTOList(personnelRepository.findAll());
    }

    @Transactional
    // --- ВАЖЛИВО: Очищення кешу при видаленні ---
    @CacheEvict(value = "personnel", key = "#id")
    public void delete(Long id) {
        if (!personnelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Не знайдено");
        }
        personnelRepository.deleteById(id);
    }

    @Transactional
    // --- ВАЖЛИВО: Очищення кешу при оновленні ---
    // Ми видаляємо старий запис з кешу, щоб при наступному getById завантажились свіжі дані
    @CacheEvict(value = "personnel", key = "#id")
    public PersonnelResponseDTO update(Long id, PersonnelUpdateDTO dto) {
        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Не знайдено"));

        personnelMapper.updateEntityFromDTO(dto, personnel);

        if (dto.getUnitId() != null) {
            MilitaryUnit unit = militaryUnitRepository.findById(dto.getUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Підрозділ не знайдено"));
            personnel.setUnit(unit);
        }

        return personnelMapper.toDTO(personnelRepository.save(personnel));
    }

    public List<PersonnelResponseDTO> getByRankAndUnit(ua.edu.viti.military.entity.Rank rank, Long unitId) {
        List<Personnel> personnelList = personnelRepository.findByRankAndUnit(rank, unitId);
        return personnelMapper.toDTOList(personnelList);
    }
}