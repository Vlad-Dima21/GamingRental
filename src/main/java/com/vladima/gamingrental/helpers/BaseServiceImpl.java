package com.vladima.gamingrental.helpers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;
import java.util.List;

@RequiredArgsConstructor
@Getter
public abstract class BaseServiceImpl<Model extends BaseModel<ModelDTO>, ModelDTO extends BaseDTO<Model>, RepositoryType extends JpaRepository<Model, Long>> implements BaseService<Model, ModelDTO> {

    private final RepositoryType repository;

    @Getter(value = AccessLevel.NONE)
    private final String LOG_NAME = getLogName();

    @Override
    public List<ModelDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(Model::toDTO)
                .toList();
    }

    @Override
    public Model getModelById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityOperationException(
                    MessageFormat.format("{0} not found", LOG_NAME),
                    MessageFormat.format("Error fetching {0} with id {1}", LOG_NAME.toLowerCase(), id),
                    HttpStatus.NOT_FOUND
                )
        );
    }

    @Override
    public ModelDTO getById(Long id) {
        return getModelById(id).toDTO();
    }

    @Override
    public void removeById(Long id) {
        repository.findById(id).orElseThrow(() ->
                new EntityOperationException(
                    MessageFormat.format("{0} does not exist", LOG_NAME),
                    MessageFormat.format("{0} with id {1} does not exist", LOG_NAME, id),
                    HttpStatus.BAD_REQUEST
                ));
        repository.deleteById(id);
    }
}
