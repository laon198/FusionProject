package domain.model;

public abstract class Member{
    protected long id;
    protected String name;
    protected String department;
    protected String birthDate;

    abstract public static class Builder{
        private long id = -1;
        private String name;
        private String department;
        private String birthDate;

        protected Builder(String name, String department, String birthDate){
            this.name = name;
            this.department = department;
            this.birthDate = birthDate;
        }

        public Builder id(long value){
            id = value;
            return self();
        }

        abstract public Member build();

        abstract protected Builder self();
    }

    protected Member(Builder builder){
        id = builder.id;
        name = builder.name;
        department = builder.department;
        birthDate = builder.birthDate;
    }

    public long getID(){
        return id;
    }
}
