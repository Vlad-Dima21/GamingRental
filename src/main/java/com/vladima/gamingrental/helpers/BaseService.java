package com.vladima.gamingrental.helpers;

import java.util.List;

public interface BaseService<Model extends BaseModel<ModelDTO>, ModelDTO extends BaseDTO<Model>> {
    List<ModelDTO> getAll();
    Model getModelById(Long id);
    ModelDTO getById(Long id);
    ModelDTO create(ModelDTO modelDTO);
    ModelDTO updateInfo(Long id, ModelDTO modelDTO);
    void removeById(Long id);
    String getLogName();
}
