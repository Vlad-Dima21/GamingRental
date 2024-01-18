# <img style="float:right" height=40 src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Spring_Framework_Logo_2018.svg/1200px-Spring_Framework_Logo_2018.svg.png"> Gaming Rental
## Business requirements

The API is intended to be used by company workers in order to manage business operations.
**Requirements**:
1. The API will support searching for existing clients
2. The API will allow registering new client
3. The API will allow updating client information
4. The API will allow fetching a specific client
5. The API will ensure the management of rental operations
6. The API will enable searching for devices
7. The API will allow accessing specific device information
8. The API will enable registering new devices
9. The API will allow device removal, along with all associated units
10. The API will allow verifying unit availability, based on device
11. The API will support searching for game copies


**MVP**:
1. The API client search functionality will either return the entire list of clients, or a filtered one. The list may be filtered by the client name, thus only returning the specific client, or by name, where an entire list with corresponding names will be provided
2. Only valid clients may be registered. Provided email and phone number must be valid. The email must be unique across all users.
3. The API will ensure that new rentals are registered only if the associated device unit/games are available. Upon rental register, all included products are marked as unavailable.
4. The API will ensure that all invalid requests, with incorrect/missing data, be rejected and provide an explanatory message.
5. The API will ensure that actions with values that do not match with what is required will be cancelled. Values that are too short or are out of the required range will lead to rejecting the request.

## API

### /api/clients/{id}

#### GET
##### Summary:

Get a client by ID

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | The client ID | Yes | long |

##### Responses

| Code | Description      | Schema                                         |
|------|------------------|------------------------------------------------|
| 200  | Client found     | ```ClientDTO```                                |
| 404  | Client not found | ```EntitiesExceptionHandler.ExceptionFormat``` |

### /api/clients

#### GET
##### Summary:

Filter clients

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| email | query | Filtered by full email | No | string |
| name | query | Filtered by name (may be contained) | No | string |

##### Responses

| Code | Description                     | Schema                                         |
|------|---------------------------------|------------------------------------------------|
| 200  | Valid filters and clients found | ```List<ClientDTO>```                          |
| 400  | Invalid filters                 | ```EntitiesExceptionHandler.ExceptionFormat``` |

### /api/clients/update/{id}

#### PUT
##### Summary:

Update client info

##### Parameters

| Name | Located in | Description   | Required | Schema |
|------|------------|---------------|----------|--------|
| id   | path       | The client ID | Yes      | long   |

##### Responses

| Code | Description                | Schema                                         |
|------|----------------------------|------------------------------------------------|
| 200  | Client info updated        | ```ClientDTO```                                |
| 404  | Client not found           | ```EntitiesExceptionHandler.ExceptionFormat``` |
| 409  | Email/Phone already in use | ```EntitiesExceptionHandler.ExceptionFormat``` |

### /api/clients/create

#### POST
##### Summary:

Add a new client

##### Responses

| Code | Description                   | Schema                                         |
|------|-------------------------------|------------------------------------------------|
| 201  | Client added                  | ```ClientDTO```                                |
| 409  | Email/Phone is already in use | ```EntitiesExceptionHandler.ExceptionFormat``` |


### /api/clients/remove/{id}

#### DELETE
##### Summary:

Remove a client

##### Parameters

| Name | Located in | Description   | Required | Schema |
|------|------------|---------------|----------|--------|
| id   | path       | The client ID | Yes      | long   |

##### Responses

| Code | Description      | Schema                                         |
|------|------------------|------------------------------------------------|
| 204  | Client removed   | no content                                     |
| 404  | Client not found | ```EntitiesExceptionHandler.ExceptionFormat``` |


### /api/rentals/return/{id}

#### PATCH
##### Summary:

Register rental return

##### Parameters

| Name | Located in | Description | Required | Schema |
|------|------------|-------------|----------|--------|
| id   | path       | Rental ID   | Yes      | long   |

##### Responses

| Code | Description                      | Schema                                         |
|------|----------------------------------|------------------------------------------------|
| 200  | Rental was returned successfully | ```RentalDTO```                                |
| 400  | Rental has been already returned | ```EntitiesExceptionHandler.ExceptionFormat``` |
| 404  | Rental was not found             | ```EntitiesExceptionHandler.ExceptionFormat``` |

### /api/rentals

#### GET
##### Summary:

Get filtered rentals

##### Parameters

| Name        | Located in | Description                          | Required | Schema  |
|-------------|------------|--------------------------------------|----------|---------|
| clientEmail | query      | Client Email                         | No       | string  |
| deviceName  | query      | Device Name                          | No       | string  |
| returned    | query      | Only rentals that have been returned | No       | boolean |
| pastDue     | query      | Only rentals that are past due       | No       | boolean |

##### Responses

| Code | Description                     | Schema                                         |
|------|---------------------------------|------------------------------------------------|
| 200  | Valid filters and rentals found | ```List<RentalDTO>```                          |
| 404  | Client or device not found      | ```EntitiesExceptionHandler.ExceptionFormat``` |

### /api/rentals/create

#### POST
##### Summary:

Register a new rental

##### Responses

| Code | Description                                                      | Schema                                         |
|------|------------------------------------------------------------------|------------------------------------------------|
| 200  | Rental was registered successfully                               | ```RentalDTO```                                |
| 400  | Device unit not available or game copies not found/not available | ```EntitiesExceptionHandler.ExceptionFormat``` |
| 404  | Client not found or device unit not found                        | ```EntitiesExceptionHandler.ExceptionFormat``` |


### /api/devices/create

#### POST
##### Summary:

Register a new device

##### Responses

| Code | Description                              |
|------|------------------------------------------|
| 200  | Device was registered                    |
| 409  | Device with the same name already exists |

### /api/devices

#### GET
##### Summary:

Get filtered devices

##### Parameters

| Name        | Located in | Description                  | Required | Schema  |
|-------------|------------|------------------------------|----------|---------|
| name        | query      | Name should contain          | No       | string  |
| producer    | query      | Producer name should contain | No       | string  |
| year        | query      | Released after the year      | No       | integer |
| ifAvailable | query      | Only available               | No       | boolean |

##### Responses

| Code | Description   | Schema                          |
|------|---------------|---------------------------------|
| 200  | Valid filters | ```List<DeviceBaseExtrasDTO>``` |

### /api/devices/{id}

#### GET
##### Summary:

Get device by ID

##### Parameters

| Name | Located in | Description | Required | Schema |
|------|------------|-------------|----------|--------|
| id   | path       | Device ID   | Yes      | long   |

##### Responses

| Code | Description      | Schema                                         |
|------|------------------|------------------------------------------------|
| 200  | Device found     | ```DeviceBaseExtrasDTO```                      |
| 404  | Device not found | ```EntitiesExceptionHandler.ExceptionFormat``` |

### /api/devices/remove/{id}

#### DELETE
##### Summary:

Remove a device

##### Parameters

| Name | Located in | Description | Required | Schema |
|------|------------|-------------|----------|--------|
| id   | path       | Device ID   | Yes      | long   |

##### Responses

| Code | Description        | Schema                                         |
|------|--------------------|------------------------------------------------|
| 204  | Device was removed | no content                                     |
| 404  | Device not found   | ```EntitiesExceptionHandler.ExceptionFormat``` |


### /api/units/of

#### GET
##### Summary:

Get device units by the device and filter by availability

##### Parameters

| Name          | Located in | Description          | Required | Schema  |
|---------------|------------|----------------------|----------|---------|
| name          | query      | Device name          | Yes      | string  |
| availableOnly | query      | Only available units | No       | boolean |

##### Responses

| Code | Description           | Schema                                         |
|------|-----------------------|------------------------------------------------|
| 200  | Device units returned | ```List<DeviceExtrasDTO>```                    |
| 404  | Device not found      | ```EntitiesExceptionHandler.ExceptionFormat``` |

### /api/units/of-id/{id}

#### GET
##### Summary:

Get device units by the device ID

##### Parameters

| Name | Located in | Description | Required | Schema |
|------|------------|-------------|----------|--------|
| id   | path       | Device ID   | Yes      | long   |

##### Responses

| Code | Description           | Schema                                         |
|------|-----------------------|------------------------------------------------|
| 200  | Device units returned | ```List<DeviceExtrasDTO>```                    |
| 404  | Device not found      | ```EntitiesExceptionHandler.ExceptionFormat``` |


### /api/games

#### GET
##### Summary:

Get filtered game copies

##### Parameters

| Name          | Located in | Description                 | Required | Schema  |
|---------------|------------|-----------------------------|----------|---------|
| gameId        | query      | Filter by game ID           | No       | long    |
| deviceId      | query      | Filter by device ID         | No       | long    |
| onlyAvailable | query      | Fetch only available copies | No       | boolean |

##### Responses

| Code | Description              | Schema                                         |
|------|--------------------------|------------------------------------------------|
| 200  | List of game copies      | ```List<GameCopyDTO>```                        |
| 404  | Game or device not found | ```EntitiesExceptionHandler.ExceptionFormat``` |