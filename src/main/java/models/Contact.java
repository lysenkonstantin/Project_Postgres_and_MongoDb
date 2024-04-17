package models;

public class Contact {
    int id;
    int customerId;
    String name;
    String phone;
    String email;

    public Contact(int id, int customerId, String name, String phone, String email) {
        this.id = id;
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "\nContact{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
