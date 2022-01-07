package domain.model;

import java.util.Objects;

public abstract class Member{
    protected long id;
    protected String name;
    protected String department;
    protected String birthDate;

    abstract public static class Builder<T extends Builder<T>>{
        private long id = -1;
        private String name;
        private String department;
        private String birthDate;

        protected Builder(String name, String department, String birthDate){
            this.name = name;
            this.department = department;
            this.birthDate = birthDate;
        }

        public T id(long value){
            id = value;
            return self();
        }

        abstract public Member build();

        abstract protected T self();
    }

    protected Member(Builder<?> builder){
        id = builder.id;
        name = builder.name;
        department = builder.department;
        birthDate = builder.birthDate;
    }

    public long getID(){
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Member)) return false;
        Member member = (Member) o;
        return id == member.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
