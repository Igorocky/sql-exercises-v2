package org.igye.sqlexercises.newclasses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Random;

public abstract class TestDataGenerator {
    private int counter = 1;
    protected Random rnd = new Random();

    abstract public String getId();
    abstract public List<String> generateTestData();

    protected int nextId() {
        return counter++;
    }

    protected void intAttr(StringBuilder sb, Integer integer) {
        if (integer == null) {
            sb.append("null");
        } else {
            sb.append(integer);
        }
    }

    protected void bigDecimalAttr(StringBuilder sb, BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            sb.append("null");
        } else {
            sb.append(bigDecimal);
        }
    }

    protected void stringAttr(StringBuilder sb, String str) {
        if (str == null) {
            sb.append("null");
        } else {
            sb.append("'").append(str).append("'");
        }
    }

    protected void dateAttr(StringBuilder sb, LocalDate date) {
        if (date == null) {
            sb.append("null");
        } else {
            sb.append("'")
                    .append(date.getYear())
                    .append("-")
                    .append(date.getMonthValue())
                    .append("-")
                    .append(date.getDayOfMonth())
                    .append("'");
        }
    }

    protected <E> E randomElem(List<E> elems) {
        return elems.get(rnd.nextInt(elems.size()));
    }

    protected LocalDate randomDate(LocalDate from, LocalDate till) {
        return from.plusDays(Math.abs(rnd.nextLong()) % (Period.between(from, till).toTotalMonths()*30 + 1));
    }

    protected double randomDouble(double from, double till) {
        return from + (till - from)*rnd.nextDouble();
    }
}
