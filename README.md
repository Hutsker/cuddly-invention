# FindLink in JSON 
Проверка файла на наличие ссылок "@"

Работа через командную строку
---
`-i` - input (папка с json файлами или json файл)<br>
`-o` - objects (файл с форматом txt в котором прописываются объекты которые необходимо найти)

**Работа с файлами:**
- `java -jar target\plsqlParser-1.0-jar-with-dependencies.jar -i "src\main\resources\sql.json" -o "src\main\resources\configuration\objects.txt"` <br>
    Вывод результата в консоль.
- Использование только одного параметра недопустимо.

Работа с папками:
- `java -jar plsqlParser-1.0-jar-with-dependencies.jar -i ".../name_folder"` <br>
    "..." глобальный путь до папки с файлами json.

    
Возможные ошибки
---
IDEA может не воспринимать PLSQLParser.java как класс и будет видеть
его как Java file. 

Чтобы это исправить:
- Help > Edit Custom Properties...
- Вставляем туда это `idea.max.intellisense.filesize=25000`
- Перезапускаем IDEA

P.S. Данная ошибка никак не влияет на компиляцию

Dependencies
---
- [Gson 2.8.6](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.6)
- [Apache Commons CLI 1.4](https://mvnrepository.com/artifact/commons-cli/commons-cli/1.4)
- [Apache Commons IO 2.7](https://mvnrepository.com/artifact/commons-io/commons-io/2.7)
