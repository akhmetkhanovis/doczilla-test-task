# DoczillaTestTask
## Задание №1
#### Имеется корневая папка. В этой папке могут находиться текстовые файлы, а также другие папки. В других папках также могут находится текстовые файлы и папки (уровень вложенности может оказаться любым). Найти все текстовые файлы, отсортировать их по имени и склеить содержимое в один текстовый файл. 
### Дополнительно:
#### В каждом файле может быть ни одной, одна или несколько директив формата:

*require ‘<путь к другому файлу от корневого каталога>’*

Директива означает, что текущий файл зависит от другого указанного файла. Необходимо выявить все зависимости между файлами, построить сортированный список, для которого выполняется условие: если файл А, зависит от файла В, то файл А находится ниже файла В в списке. Осуществить конкатенацию файлов в соответствии со списком. Если такой список построить невозможно (существует циклическая зависимость), программа должна вывести соответствующее сообщение.
### Решение:
#### Для демонстрации решения необходимо запустить метод main() в классе Demo.
#### Для проверки выброса ошибки при циклической зависимости файлов в файле Folder 1/File 1-1 нужно "раскомментировать" строку //require 'Folder 1/File 1-2' (удалить "//") и снова запустить приложение.