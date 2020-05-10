package models;

import java.util.Objects;

public class Cursa implements Entity<Integer> {
    private Integer id;
    private int capacitateMotor;


    public Cursa(Integer id, int capacitateMotor) {
        this.id = id;
        this.capacitateMotor = capacitateMotor;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public int getCapacitateMotor() {
        return capacitateMotor;
    }

    public void setCapacitateMotor(int capacitateMotor) {
        this.capacitateMotor = capacitateMotor;
    }

    @Override
    public String toString() {
        return "Cursa{" +
                "id=" + id +
                ", capacitateMotor=" + capacitateMotor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cursa cursa = (Cursa) o;
        return capacitateMotor == cursa.capacitateMotor &&
                Objects.equals(id, cursa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, capacitateMotor);
    }
}
