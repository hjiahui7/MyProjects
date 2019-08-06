import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * create a reader to read the input and excuse
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class Reader {

    /**
     * the field
     */
    private MemManager mem;
    private HashTable<String, Handle> hashTable;


    /**
     * the constructor for reader class
     * 
     * @param meSize
     *            the memory size
     * @param hashS
     *            the hashtable size
     */
    public Reader(int meSize, int hashS) {
        mem = new MemManager(meSize);
        hashTable = new HashTable<String, Handle>(hashS);
    }


    /**
     * read record from the file
     * 
     * @param fileName
     *            file name which contain the record
     * @throws FileNotFoundException
     *             throws when the file is not found
     */
    public void reader(String fileName) throws FileNotFoundException {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(new File(fileName));
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String content = line.replaceAll("\\s{1,}", " ").trim();
            if (content.startsWith("print hashtable")) {
                this.printHashTable();
            }
            else if (content.startsWith("print blocks")) {
                this.printBlocks();
            }
            else if (content.startsWith("update add")) {
                this.updateAdd(content);
            }
            else if (content.startsWith("add")) {
                this.add(content);

            }
            else if (content.startsWith("update delete")) {
                this.updateRemove(content);
            }
            else if (content.startsWith("delete")) {
                this.delete(content);
            }
        }
    }


    /**
     * update a record in the memory pool, it will search the name first then
     * add the field name and value into that name
     * 
     * @param data
     *            the string data
     * @return a true or false
     */
    public boolean updateAdd(String data) {
        String content = data.replaceFirst("update add", " ").trim();
        String[] order = content.split("<SEP>");
        int index = this.hashTable.contains(order[0].trim());
        if (index == -1) {
            System.out.println("|" + order[0]
                + "| not updated because it does not exist "
                + "in the Name database.");
            return false;
        }
        Handle handle = this.hashTable.getData(index).getValue();
        byte[] record = new byte[handle.getSize()];
        this.mem.get(record, handle, handle.getSize());
        String name = new String(record);
        String[] dataName = name.split("<SEP>");
        StringBuilder str = new StringBuilder();
        str.append(name);
        boolean find = false;
        boolean duplicate = false;
        if (dataName.length > 1) {
            for (int i = 1; i < dataName.length; i = i + 2) {
                if (dataName[i].equals(order[1])) {
                    find = true;
                    if (!dataName[i + 1].equals(order[2])) {
                        StringBuilder deleteString = new StringBuilder();
                        deleteString.append("<SEP>").append(dataName[i].trim())
                            .append("<SEP>").append(dataName[i + 1].trim());
                        name = name.replaceAll(deleteString.toString(), "");
                        StringBuilder addString = new StringBuilder();
                        addString.append(name);
                        addString.append("<SEP>").append(order[1].trim())
                            .append("<SEP>").append(order[2].trim());
                        str = addString;
                        break;
                    }
                    else {
                        duplicate = true;
                    }
                }
            }
        }
        if (!find) {
            str.append("<SEP>");
            str.append(order[1].trim());
            str.append("<SEP>");
            str.append(order[2].trim());
        }
        if (duplicate) {
            System.out.println("Updated Record: |" + name + "|");
            return false;
        }
        name = str.toString();
        mem.remove(handle);
        byte[] indata = name.getBytes();
        Handle newHandle = mem.insert(indata, indata.length);
        handle.setSize(newHandle.getSize());
        handle.setOffset(newHandle.getOffset());
        System.out.println("Updated Record: |" + name + "|");
        return true;
    }


    /**
     * update delete a record if it in the database, if not exit, then the
     * method will return false
     * 
     * @param data
     *            string which contain the detail
     * @return true or false
     */
    public boolean updateRemove(String data) {
        String content = data.replaceFirst("update delete", "").trim();
        String[] order = content.split("<SEP>");
        int index = this.hashTable.contains(order[0].trim());
        if (index == -1) {
            System.out.println("|" + order[0]
                + "| not updated because it does not exist "
                + "in the Name database.");
            return false;
        }
        Handle handle = this.hashTable.getData(index).getValue();
        byte[] record = new byte[handle.getSize()];
        this.mem.get(record, handle, handle.getSize());
        String name = new String(record);
        boolean find = false;
        String[] nameData = name.split("<SEP>");
        if (nameData.length > 1) {
            StringBuilder str = new StringBuilder();
            for (int i = 1; i < nameData.length; i += 2) {
                String thisString = nameData[i].trim();
                if (thisString.equals(order[1])) {
                    find = true;
                    str.append("<SEP>");
                    str.append(nameData[i].trim());
                    str.append("<SEP>");
                    str.append(nameData[i + 1].trim());
                    name = name.replaceAll(str.toString(), "");
                }
            }
        }
        if (!find) {
            StringBuilder str = new StringBuilder();
            str.append("|").append(order[0]).append("| ").append(
                "not updated because the field |").append(order[1].trim())
                .append("| does not exist");
            System.out.println(str.toString());
            return false;
        }
        mem.remove(handle);
        byte[] indata = name.getBytes();
        Handle newHandle = mem.insert(indata, indata.length);
        handle.setSize(newHandle.getSize());
        handle.setOffset(newHandle.getOffset());
        System.out.println("Updated Record: |" + name + "|");
        return true;
    }


    /**
     * add the data into the database and the memory pool
     * 
     * @param name
     *            string which contain the detail
     * @return true or false
     */
    public boolean add(String name) {
        name = name.replaceFirst("add", "").trim();
        if (this.hashTable.contains(name) == -1) {
            byte[] data = name.getBytes();
            Handle value = this.mem.insert(data, data.length);
            hashTable.add(name, value);
            System.out.println("|" + name
                + "| has been added to the Name database.");
            return true;
        }
        else {
            System.out.println("|" + name
                + "| duplicates a record already in the Name database.");
        }

        return false;
    }


    /**
     * delete the data from memory pool and database
     * 
     * @param str
     *            string which contain the detail
     * @return true or false
     */
    public boolean delete(String str) {
        String name = str.replaceFirst("delete", "").trim();
        int index = hashTable.contains(name);
        if (index != -1) {
            String key = hashTable.getData(index).getKey();
            Handle data = hashTable.getData(index).getValue();
            mem.remove(data);
            this.hashTable.remove(key);
            System.out.println("|" + name
                + "| has been deleted from the Name database.");
            return true;
        }
        System.out.println("|" + name
            + "| not deleted because it does not exist in the Name database.");
        return false;
    }


    /**
     * print the hash table detail
     */
    public void printHashTable() {
        System.out.println(this.hashTable.toString());
    }


    /**
     * print the memory block detail
     */
    public void printBlocks() {
        mem.printBlocks();
    }
}
