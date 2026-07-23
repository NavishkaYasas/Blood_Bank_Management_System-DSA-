package bloodbank.model;

public abstract class Person {
    private String id;
    private String name;
    private int age;
    private String contactNo;
    private String address;

    public Person(String id, String name, int age, String contactNo, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contactNo = contactNo;
        this.address = address;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Age: " + age
                + " | Contact: " + contactNo + " | Address: " + address;
    }
}