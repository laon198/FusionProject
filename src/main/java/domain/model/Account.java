package domain.model;


import java.util.Objects;

public class Account {
    private long pk;
    private String id;
    private String password;
    private String position;
    private long memberID;

    public static class Builder{
        private long pk=-1;
        private String id;
        private String password;
        private String position;
        private long memberID;

        private Builder(String id, String password,
                            String position, long memberID){
            this.id = id;
            this.password = password;
            this.position = position;
            this.memberID = memberID;
        }

        public Builder pk(long value){
            pk = value;
            return this;
        }

        public Account build(){
            return new Account(this);
        }
    }

    public static Builder builder(String id, String password,
                                  String position, long memberID){
        return new Builder(id, password, position, memberID);
    }

    private Account(Builder builder) {
        pk = builder.pk;
        id = builder.id;
        password = builder.password;
        memberID = builder.memberID;
        position = builder.position;
    }

    public boolean checkPassword(String pwd){
        return password.equals(pwd);
    }

    public String getPosition(){
        return position;
    }

    public void changePassword(String pwd){
        password = pwd;
    }

    @Override
    public String toString() {
        return "Account{" +
                "pk=" + pk +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", position='" + position + '\'' +
                ", memberID=" + memberID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Account)) return false;
        Account account = (Account) o;
        return pk == account.pk;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk);
    }
}
