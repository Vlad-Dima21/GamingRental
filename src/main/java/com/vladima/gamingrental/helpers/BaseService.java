package com.vladima.gamingrental.helpers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface BaseService<Model extends BaseModel<ModelDTO>, ModelDTO extends BaseDTO<Model>> {
    Page<ModelDTO> getAll(PageRequest pageRequest);
    Model getModelById(Long id);
    ModelDTO getById(Long id);
    ModelDTO create(ModelDTO modelDTO);
    ModelDTO updateInfo(Long id, ModelDTO modelDTO);
    void removeById(Long id);
    String getLogName();
}
