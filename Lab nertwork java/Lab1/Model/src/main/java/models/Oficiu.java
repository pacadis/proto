package models;

import java.io.Serializable;
import java.util.Objects;

public class Oficiu implements Comparable<Oficiu>, Serializable,Entity<Integer> {
    private Integer id;
    private String user;
    private String parola;

    public Oficiu(Integer id, String user, String parola) {
        this.id = id;
        this.user = user;
        this.parola = parola;
    }

    public Oficiu(String user) {
        this.id = 0;
        this.user = user;
        this.parola = "";
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String toString() {
        String string="Angajat "+user+" cu id-ul "+id;
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Oficiu oficiu = (Oficiu) o;
        return Objects.equals(id, oficiu.id) &&
                Objects.equals(user, oficiu.user) &&
                Objects.equals(parola, oficiu.parola);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, parola);
    }

    public DTOAngajat convert(){
        DTOAngajat rez = new DTOAngajat(this.user,this.parola);
        return rez;
    }

    @Override
    public int compareTo(Oficiu o) {
        return this.user.compareTo(o.user);
    }
}
