package domain.model;

public class Admin extends Member {
    private String adminCode;

    public static class Builder extends Member.Builder<Builder>{
        private String adminCode;

        private Builder(String name, String department, String birthDate,
                            String adminCode){
            super(name, department, birthDate);
            this.adminCode = adminCode;
        }

        public Admin build(){
            return new Admin(this);
        }

        protected Builder self(){
            return this;
        }
    }

    public static Builder builder(String name, String department, String birthDate,
                                  String adminCode){
        return new Builder(name, department, birthDate, adminCode);
    }

    private Admin(Builder builder){
        super(builder);
        adminCode=builder.adminCode;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminCode='" + adminCode + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", birthDate='" + birthDate + '\'' +
                '}';
    }
}