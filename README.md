# MyStore API
Программа представляет небольшой API для ведения складского учета.  
Функционал:
- [x] Просмотр, создание, редактирование и удаление товаров/складов.
- [x] Поступление, отпуск, перемещение товарных остатков при помощи входящих документов.
- [x] Формирование отчетов.  

## Стэк
WildFly 27, Jakarta EE, H2

## Старт
Сборка проекта, создание standalone-сервера и развертывание приложения:  
`mvn wildfly:run` 

# Описание эндпоинтов
## Сущности
### 1) ТОВАР
Атрибуты сущности  

| Название  | Тип | Описание |
| --- | --- | --- |
| **id**  | UUID  | Идентификатор `Обязателен при ответе` |
| **article** | String(100) | Артикул `Обязателен при ответе` `Необходим при создании`   |
| **name** | String(100) | Наименование `Обязателен при ответе` `Необходим при создании`|
| **lastPurchasePrice** | BigDecimal | Цена последней закупки. По умолчанию **0.00** `Обязателен при ответе` |
| **lastSellingPrice** | BigDecimal | Цена последней продажи. По умолчанию **0.00** `Обязателен при ответе` |
| **positions** | ArrayObject | Товарные позиции на складах. В каждую позицию вложено поле остатка **stock** |

#### Просмотр всех созданных товаров
```shell
curl -X GET "http://.../MoySklad/api/1.0/entity/product"
```
Ответ: Response 200 Успешный запрос. Результат - предоставление списка товаров  
```json 
[  
    {  
        "id": "69e30cc0-5f38-40cd-9bcf-b6c96952444d",  
        "article": "123",  
        "name": "My product1",  
        "lastPurchasePrice": 5.99,  
        "lastSellingPrice": 0.00  
    },  
    {  
        "id": "c8c18907-27e8-446f-9f62-5651a4014a19",
        "article": "1234",
        "name": "My product2",
        "lastPurchasePrice": 533.59,
        "lastSellingPrice": 0.00
    },
    {
        "id": "785fdf88-b259-47e4-bee0-26507f61f0d0",
        "article": "12345",
        "name": "My product3",
        "lastPurchasePrice": 0.00,
        "lastSellingPrice": 0.00
    }
]
```

#### Создание товара/товаров без принадлежности к складу
```shell
curl -X POST "http://.../MoySklad/api/1.0/entity/product
" -H "Content-Type: application/json" 
-d '[
	  {
		  "name": "My product",
		  "stock": "123",
		  "article": "12345",
		  "lastPurchasePrice": "1.00",
		  "lastSellingPrice": "4.00"
	  }
]'
```
Ответ: Response 200 Успешный запрос. Результат - предоставление списка созданных/обновленных товаров  
```json
[
    {
        "id": "8b9183c1-a031-4fe1-b2dc-bd19dc4164b6",
        "article": "12345",
        "name": "My product",
        "lastPurchasePrice": 1.00,
        "lastSellingPrice": 4.00,
        "stock": 123
    }
]
```
#### Обновление товара
Вариант с указанием ID в поле передаваемого обьекта  
```shell
curl -X POST "http://localhost:8080/MoySklad/api/1.0/entity/product
" -H "Content-Type: application/json" 
-d '[
		{
			"id": "1c4f4d3b-55d6-4e3d-8e3d-18ee7a2067c4",
			"name": "My updated product",
            "article": "updated123333"
		}
]'
```
Ответ: Response 200 Успешный запрос. Результат - предоставление списка обновленных товаров. Поля, изменения для которых не передавались, остаются прежними  
```json
[
    {
        "id": "1c4f4d3b-55d6-4e3d-8e3d-18ee7a2067c4",
        "article": "updated123333",
        "name": "My updated product",
        "lastPurchasePrice": 10.50,
        "lastSellingPrice": 20.99
    }
]
```
Bариант с указанием ID в запросе
```shell
curl -X PUT http://.../MoySklad/api/1.0/entity/product/1c4f4d3b-55d6-4e3d-8e3d-18ee7a2067c4
" -H "Content-Type: application/json" 
-d '{
        "name": "My updated product",
        "article": "в334-ffff",
}'
```
Ответ: Response 200 Успешный запрос. Результат - предоставление обновленного товара. Поля, изменения для которых не передавались, остаются прежними
```json
{
    "id": "1c4f4d3b-55d6-4e3d-8e3d-18ee7a2067c4",
    "article": "в334-ffff",
    "name": "My updated product",
    "lastPurchasePrice": 1.00,
    "lastSellingPrice": 4.00
}
```

#### Удаление товара
```shell
curl -X DELETE "http://.../MoySklad/api/1.0/entity/product/2d1b5d13-8ecc-465e-9a4e-a0900e69f777"
```
Ответ: Response 200 Успешный запрос. Результат - успешное удаление товара  

### 2) СКЛАД
Атрибуты сущности

| Название  | Тип | Описание |
| --- | --- | --- |
| **id**  | UUID  | Идентификатор `Обязателен при ответе` |
| **name** | String(100) | Наименование `Обязателен при ответе` `Необходим при создании` |
| **positions** | ArrayObject | Товарные позиции на складах. В каждую позицию вложено поле остатка **stock** |

#### Просмотр всех складов
```shell
curl -X GET "http://.../MoySklad/api/1.0/entity/store"
```
Ответ: Response 200 Успешный запрос. Результат - предоставление списка складов
```json
[
    {
        "id": "51f93e82-1d6f-45e6-89ca-547ee47b9d87",
        "name": "My store4",
        "products": []
    },
    {
        "id": "6fcb4d0c-7ca9-471c-8180-3a5aef509a19",
        "name": "My store5",
        "products": []
    }
]
```

#### Создание склада/складов
```shell
curl -X POST "http://localhost:8080/MoySklad/api/1.0/entity/store"
-H "Content-Type: application/json" 
-d '[
	{
		"name": "My store1"
	},
	{
		"name":"My store2"
	}
]'
```
Ответ: Response 200 Успешный запрос. Результат - предоставление списка созданных складов
```json
[
    {
        "id": "34c62e0e-e24d-4179-8f87-5d6178e51520",
        "name": "My store1"
    },
    {
        "id": "6fcb4d0c-7ca9-471c-8180-3a5aef509a19",
        "name": "My store2"
    }
]
```

#### Обновление склада
Вариант с указанием ID в поле передаваемого обьекта
```shell
curl -X POST "http://.../MoySklad/api/1.0/entity/store"
-H "Content-Type: application/json" 
-d '[
	{
        "id": "34c62e0e-e24d-4179-8f87-5d6178e51520",
		"name": "My updated"
	}
]'
```
Ответ: Response 200 Успешный запрос. Результат - предоставление обновленного склада
```json
[
    {
        "id": "34c62e0e-e24d-4179-8f87-5d6178e51520",
        "name": "My updated"
    }
]
```
Вариант с указанием ID в запросе
```shell
curl -X PUT "http://.../MoySklad/api/1.0/entity/store/163897e0-32bf-4eda-aa97-fc35b8f3933e"
-H "Content-Type: application/json" 
-d '{
        "name": "My updated store1"
}'
```
Ответ: Response 200 Успешный запрос. Результат - предоставление обновленного склада
```json
{
    "id": "163897e0-32bf-4eda-aa97-fc35b8f3933e",
    "name": "My updated store1"
}
```

#### Удаление склада
**NB:** НЕ влечет удаление связанных товаров
```shell
curl -X DELETE "http://.../MoySklad/api/1.0/entity/store/2d1b5d13-8ecc-465e-9a4e-a0900e69f777"
```
Ответ: Response 200 Успешный запрос. Результат - успешное удаление склада.

### 3) ДОКУМЕНТ
#### 3.1) ПОСТУПЛЕНИЕ ТОВАРА
Атрибуты сущности

| Название  | Тип | Описание |
| --- | --- | --- |
| **id**  | UUID  | Идентификатор `Обязателен при ответе` |
| **code** | String(100) | Уникальный код `Обязателен при ответе` `Необходим при создании`   |
| **storeID** | String | Идентификатор текущего склада `Обязателен при ответе` `Необходим при создании`|
| **positions** | ArrayObject | Позиции товаров`Обязателен при ответе` `Необходим при создании` |
| **created** | Date | Дата создания |

Атрибуты вложенного объекта внутри списка `positions`

| Название  | Тип | Описание |
| --- | --- | --- |
| **productID**  | String  | Идентификатор товара `Обязателен при ответе` `Необходим при создании` |
| **quantity** | BigDecimal | Количество `Обязателен при ответе` `Необходим при создании`   |
| **purchasePrice** | BigDecimal | Цена закупки `Обязателен при ответе` `Необходим при создании`|


##### Заведение нового поступления
Товар с ID, указанным в теле вложенного списка обьекта 'positions', будет добавлен на склад c идентификатором 'storeID' в указанном количестве.
Цена последней закупки продукта будет обновлена.
```shell
curl -X POST "http://.../MoySklad/api/1.0/entity/document/supply"
-H "Content-Type: application/json" 
-d '{
    "code": "12345-ee",
	"storeID":"ab4dd90b-fa88-43e9-aaf9-b25f1c9f619f",
    "positions": [
        {
            "productID": "a5ba1c2d-99c3-4a0e-b76b-1b386caa3383",
            "quantity": 999,
            "purchasePrice": 199.99
        }
    ]
}'
```
Ответ: Response 200 Успешный запрос. Результат - созданное поступление на склад
```json
{
    "id": "4bf31e45-87c9-41e9-9cf0-6379ae9ddf0f",
    "code": "12345-ee",
	"storeID":"ab4dd90b-fa88-43e9-aaf9-b25f1c9f619f",
    "positions": [
        {
            "productID": "a5ba1c2d-99c3-4a0e-b76b-1b386caa3383",
            "quantity": 999,
            "purchasePrice": 199.99
        }
    ]
}
```
##### Просмотр всех поступлений
```shell
curl -X GET "http://.../MoySklad/api/1.0/entity/document/supply"
```
Ответ: Response 200 Успешный запрос. Результат - список всех поступлений в обратном хронологическом порядке
```json
[
    {
        "id": "68ac6302-7bad-4efb-a305-0571d10bdab8",
        "code": "12345696-ee",
        "storeID": "c455f296-67d9-4e3f-9286-c8af036be520",
        "positions": [
            {
                "productID": "69e30cc0-5f38-40cd-9bcf-b6c96952444d",
                "quantity": 9.00,
                "purchasePrice": 5.99
            },
            {
                "productID": "c8c18907-27e8-446f-9f62-5651a4014a19",
                "quantity": 15.00,
                "purchasePrice": 533.59
            }
        ]
    },
    {
        "id": "9f4baca0-40a8-4c63-a22f-e96b949c1dd2",
        "code": "1234569333-ee",
        "storeID": "c455f296-67d9-4e3f-9286-c8af036be520",
        "positions": [
            {
                "productID": "69e30cc0-5f38-40cd-9bcf-b6c96952444d",
                "quantity": 99.00,
                "purchasePrice": 5.99
            }
        ]
    }
]
```
#### 3.2) ОТПУСК ТОВАРА
Атрибуты сущности

| Название  | Тип | Описание |
| --- | --- | --- |
| **id**  | UUID  | Идентификатор `Обязателен при ответе` |
| **code** | String(100) | Уникальный код `Обязателен при ответе` `Необходим при создании`   |
| **storeID** | String | Идентификатор текущего склада `Обязателен при ответе` `Необходим при создании`|
| **positions** | ArrayObject | Позиции товаров`Обязателен при ответе` `Необходим при создании` |
| **created** | Date | Дата создания |

Атрибуты вложенного объекта внутри списка `positions`

| Название  | Тип | Описание |
| --- | --- | --- |
| **productID**  | String  | Идентификатор товара `Обязателен при ответе` `Необходим при создании` |
| **quantity** | BigDecimal | Количество `Обязателен при ответе` `Необходим при создании`   |
| **sellingPrice** | BigDecimal | Цена продажи `Обязателен при ответе` `Необходим при создании`|

##### Заведение новой продажи
```shell
curl -X POST "http://.../MoySklad/api/1.0/entity/document/sale"
-H "Content-Type: application/json" 
-d '{
    "code":"12345695554-ee",
    "storeID":"c455f296-67d9-4e3f-9286-c8af036be520",
    "positions": [
        {
            "productID":"c8c18907-27e8-446f-9f62-5651a4014a19",
            "quantity":"4",
            "sellingPrice":"23.50"
        }
    ]
}
```
Ответ: Response 200 Успешный запрос. Результат - созданная продажа со склада. Цена последней продажи продукта будет обновлена.
```json

{
  "id": "ddc1f4cb-8f48-494a-aa90-05e073905e5e",
  "code": "12345695554-ee",
  "storeID": "c455f296-67d9-4e3f-9286-c8af036be520",
  "positions": [
    {
      "productID": "c8c18907-27e8-446f-9f62-5651a4014a19",
      "quantity": "4",
      "sellingPrice": "23.50"
    }
  ]
}
```
##### Просмотр всех продаж
```shell
curl -X GET "http://.../MoySklad/api/1.0/entity/document/supply"
```
Ответ: Response 200 Успешный запрос. Результат - список всех продаж в обратном хронологическом порядке  
```json
[
    {
        "id": "ddc1f4cb-8f48-494a-aa90-05e073905e5e",
        "code": "12345695554-ee",
        "storeID": "62b8fefb-fbc5-4626-a34e-873fbb9bc07f",
        "positions": [
            {
                "productID": "2c19fba1-42a9-455f-a454-817d9319b9e1",
                "quantity": 3.00,
                "sellingPrice": 33.50
            },
            {
                "productID": "40922138-a0c2-4cdf-832d-dab3b1a21afa",
                "quantity": 4.00,
                "sellingPrice": 733.99
            }
        ]
    }
]
```


#### 3.2) ПЕРЕМЕЩЕНИЕ ТОВАРА
Атрибуты сущности

| Название  | Тип | Описание |
| --- | --- | --- |
| **id**  | UUID  | Идентификатор `Обязателен при ответе` |
| **code** | String(100) | Уникальный код `Обязателен при ответе` `Необходим при создании`   |
| **storeID** | String | Идентификатор текущего склада `Обязателен при ответе` `Необходим при создании` |
| **storeID** | String | Идентификатор склада-назначения `Обязателен при ответе` `Необходим при создании` |
| **positions** | ArrayObject | Позиции товаров`Обязателен при ответе` `Необходим при создании` |
| **created** | Date | Дата создания |

Атрибуты вложенного объекта внутри списка `positions`

| Название  | Тип | Описание |
| --- | --- | --- |
| **productID**  | String  | Идентификатор товара `Обязателен при ответе` `Необходим при создании` |
| **quantity** | BigDecimal | Количество `Обязателен при ответе` `Необходим при создании`   |

##### Заведение нового перемещения
Товар с ID, указанным в теле вложенного списка обьекта 'positions', будет перемещен со склада c идентификатором 'storeID'
в указанном количестве на склад с идентификатором 'toStoreID'.  
```shell
curl -X POST "http://.../MoySklad/api/1.0/entity/document/move"
-H "Content-Type: application/json" 
-d '{
    "code": "12345-ee",
	"storeID":"ab4dd90b-fa88-43e9-aaf9-b25f1c9f619f",
	"toStoreID":"c455f296-67d9-4e3f-9286-c8af036be520",
    "positions": [
        {
            "productID": "a5ba1c2d-99c3-4a0e-b76b-1b386caa3383",
            "quantity": 999
        }
    ]
}'
```
Ответ: Response 200 Успешный запрос. Результат - созданное перемещение
```json
{
    "id": "4bf31e45-87c9-41e9-9cf0-6379ae9ddf0f",
    "code": "12345-ee",
	"storeID":"ab4dd90b-fa88-43e9-aaf9-b25f1c9f619f",
    "toStoreID":"c455f296-67d9-4e3f-9286-c8af036be520",
    "positions": [
        {
            "productID": "a5ba1c2d-99c3-4a0e-b76b-1b386caa3383",
            "quantity": 999
        }
    ]
}
```

##### Просмотр всех перемещений
```shell
curl -X GET "http://.../MoySklad/api/1.0/entity/document/move"
```
Ответ: Response 200 Успешный запрос. Результат - список всех продаж в обратном хронологическом порядке
```json
[
    {
        "id": "ddc1f4cb-8f48-494a-aa90-05e073905e5e",
        "code": "12345695554-ee",
        "storeID": "62b8fefb-fbc5-4626-a34e-873fbb9bc07f",
        "toStoreID":"c455f296-67d9-4e3f-9286-c8af036be520",
        "positions": [
            {
                "productID": "2c19fba1-42a9-455f-a454-817d9319b9e1",
                "quantity": 3.00
            },
            {
                "productID": "40922138-a0c2-4cdf-832d-dab3b1a21afa",
                "quantity": 4.00
            }
        ]
    }
]
```






## Отчеты
#### По продуктам
Запрос списка всех товаров **на складах** (артикул, наименование, цены закупки и продажи)
```shell
curl -X GET "http://.../MoySklad/api/1.0/report/product/all"
```
Ответ: Response 200 Успешный запрос. Результат - список товаров
```json
[
    {
        "article": "123",
        "name": "product1",
        "lastPurchasePrice": 1.00,
        "lastSellingPrice": 2.00
    },
    {
        "article": "1234",
        "name": "product2",
        "lastPurchasePrice": 13.00,
        "lastSellingPrice": 16.00
    },
    {
        "article": "12345",
        "name": "product3",
        "lastPurchasePrice": 3.00,
        "lastSellingPrice": 5.00
    }
]
```
Фильтр по наименованию товара опционально
```shell
curl -X GET "http://.../MoySklad/api/1.0/report/product/all?filter=product2"
```
Ответ: Response 200 Успешный запрос. Результат - список всех созданных товаров, имеющих указанное наименование,
в таблице без учета привязки к складу

#### По остатку на складах
```shell
curl -X GET "http://.../MoySklad/api/1.0/report/stock/byProduct"
```
Ответ: Response 200 Успешный запрос. Результат - список товаров c общим остатком на всех складах
```json
[
    {
        "productArticle": "12345",
        "productName": "product1",
        "stock": 20.00
    },
    {
        "productArticle": "54321",
        "productName": "product2",
        "stock": 5.00
    }
]
```
Фильтр по ID склада опционально
```shell
curl -X GET "http://.../MoySklad/api/1.0/report/stock/byProduct?filter=be54f86f-efb5-4edb-b7e8-73e8f959b06f"
```
Ответ: Response 200 Успешный запрос. Результат - список товаров c остатком на указанном складе