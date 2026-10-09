
class Guest extends Person {

    private String phone;

    public Guest(int id, String name, String phone) {
        super(id, name);
        this.phone = phone;
    }

    @Override
    public void displayDetails() {
        System.out.println("Guest ID: " + getId());
        System.out.println("Guest Name: " + getName());
        System.out.println("Phone: " + phone);
    }
}
