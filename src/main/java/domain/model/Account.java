package domain.model;


public class Account {
    private long pk;
    private String id;
    private String password;
    private long memberID;

    public static class Builder{
        private long pk=-1;
        private String id;
        private String password;
        private long memberID;

        public Builder pk(long value){
            pk = value;
            return this;
        }

        public Builder memberID(long value){
            memberID = value;
            return this;
        }

        public Builder id(String value){
            id = value;
            return this;
        }

        public Builder password(String value){
            password = value;
            return this;
        }

        public Account build(){
            return new Account(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private Account(Builder builder) {
        pk = builder.pk;
        id = builder.id;
        password = builder.password;
        memberID = builder.memberID;
    }
}
