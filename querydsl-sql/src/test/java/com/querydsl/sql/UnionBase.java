package com.querydsl.sql;

import static com.querydsl.core.Target.*;
import static com.querydsl.sql.Constants.employee;
import static org.assertj.core.api.Assertions.assertThat;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.Tuple;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.domain.Employee;
import com.querydsl.sql.domain.QEmployee;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

public class UnionBase extends AbstractBaseTest {

  @SuppressWarnings("unchecked")
  @Test
  @ExcludeIn({MYSQL, TERADATA})
  public void in_union() {
    assertThat(
            query()
                .from(employee)
                .where(
                    employee.id.in(
                        query()
                            .union(
                                query().select(Expressions.ONE), query().select(Expressions.TWO))))
                .select(Expressions.ONE)
                .fetchFirst())
        .isNotNull();
  }

  @Test
  @SuppressWarnings("unchecked")
  @ExcludeIn(FIREBIRD) // order is not properly supported
  public void union() throws SQLException {
    SubQueryExpression<Integer> sq1 = query().from(employee).select(employee.id.max().as("ID"));
    SubQueryExpression<Integer> sq2 = query().from(employee).select(employee.id.min().as("ID"));
    assertThat(query().union(sq1, sq2).orderBy(employee.id.asc()).fetch())
        .isEqualTo(
            Arrays.asList(
                query().select(employee.id.min()).from(employee).fetchFirst(),
                query().select(employee.id.max()).from(employee).fetchFirst()));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void union_list() throws SQLException {
    SubQueryExpression<Integer> sq1 = query().from(employee).select(employee.id.max());
    SubQueryExpression<Integer> sq2 = query().from(employee).select(employee.id.min());
    assertThat(query().union(sq1, sq2).list()).isEqualTo(query().union(sq1, sq2).fetch());
  }

  @Test
  @SuppressWarnings("unchecked")
  public void union_all() {
    SubQueryExpression<Integer> sq1 = query().from(employee).select(employee.id.max());
    SubQueryExpression<Integer> sq2 = query().from(employee).select(employee.id.min());
    List<Integer> list = query().unionAll(sq1, sq2).fetch();
    assertThat(list).isNotEmpty();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void union_multiple_columns() throws SQLException {
    SubQueryExpression<Tuple> sq1 =
        query().from(employee).select(employee.firstname, employee.lastname);
    SubQueryExpression<Tuple> sq2 =
        query().from(employee).select(employee.lastname, employee.firstname);
    List<Tuple> list = query().union(sq1, sq2).fetch();
    assertThat(list).isNotEmpty();
    for (Tuple row : list) {
      assertThat(row.get(0, Object.class)).isNotNull();
      assertThat(row.get(1, Object.class)).isNotNull();
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  @ExcludeIn(DERBY)
  public void union_multiple_columns2() throws SQLException {
    SubQueryExpression<Tuple> sq1 =
        query().from(employee).select(employee.firstname, employee.lastname);
    SubQueryExpression<Tuple> sq2 =
        query().from(employee).select(employee.firstname, employee.lastname);
    SQLQuery<?> query = query();
    query.union(sq1, sq2);
    List<String> list = query.select(employee.firstname).fetch();
    assertThat(list).isNotEmpty();
    for (String row : list) {
      assertThat(row).isNotNull();
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  @ExcludeIn(DERBY)
  public void union_multiple_columns3() throws SQLException {
    SubQueryExpression<Tuple> sq1 =
        query().from(employee).select(employee.firstname, employee.lastname);
    SubQueryExpression<Tuple> sq2 =
        query().from(employee).select(employee.firstname, employee.lastname);
    SQLQuery<?> query = query();
    query.union(sq1, sq2);
    List<Tuple> list = query.select(employee.lastname, employee.firstname).fetch();
    assertThat(list).isNotEmpty();
    for (Tuple row : list) {
      System.out.println(row.get(0, String.class) + " " + row.get(1, String.class));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void union_empty_result() throws SQLException {
    SubQueryExpression<Integer> sq1 =
        query().from(employee).where(employee.firstname.eq("XXX")).select(employee.id);
    SubQueryExpression<Integer> sq2 =
        query().from(employee).where(employee.firstname.eq("YYY")).select(employee.id);
    List<Integer> list = query().union(sq1, sq2).fetch();
    assertThat(list).isEmpty();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void union2() throws SQLException {
    List<Integer> list =
        query()
            .union(
                query().from(employee).select(employee.id.max()),
                query().from(employee).select(employee.id.min()))
            .fetch();
    assertThat(list).isNotEmpty();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void union3() throws SQLException {
    SubQueryExpression<Tuple> sq3 =
        query().from(employee).select(new Expression[] {employee.id.max()});
    SubQueryExpression<Tuple> sq4 =
        query().from(employee).select(new Expression[] {employee.id.min()});
    List<Tuple> list2 = query().union(sq3, sq4).fetch();
    assertThat(list2).isNotEmpty();
  }

  @SuppressWarnings("unchecked")
  @Test
  @ExcludeIn({DERBY})
  public void union4() {
    SubQueryExpression<Tuple> sq1 = query().from(employee).select(employee.id, employee.firstname);
    SubQueryExpression<Tuple> sq2 = query().from(employee).select(employee.id, employee.firstname);
    assertThat(query().union(employee, sq1, sq2).select(employee.id.count()).fetch()).hasSize(1);
  }

  // FIXME for CUBRID
  // Teradata: The ORDER BY clause must contain only integer constants.
  @SuppressWarnings("unchecked")
  @Test
  @ExcludeIn({DERBY, CUBRID, FIREBIRD, TERADATA})
  @Ignore // FIXME
  public void union5() {
    /* (select e.ID, e.FIRSTNAME, superior.ID as sup_id, superior.FIRSTNAME as sup_name
     * from EMPLOYEE e join EMPLOYEE superior on e.SUPERIOR_ID = superior.ID)
     * union
     * (select e.ID, e.FIRSTNAME, null, null from EMPLOYEE e)
     * order by ID asc
     */
    QEmployee superior = new QEmployee("superior");
    SubQueryExpression<Tuple> sq1 =
        query()
            .from(employee)
            .join(employee.superiorIdKey, superior)
            .select(
                employee.id,
                employee.firstname,
                superior.id.as("sup_id"),
                superior.firstname.as("sup_name"));
    SubQueryExpression<Tuple> sq2 =
        query().from(employee).select(employee.id, employee.firstname, null, null);
    List<Tuple> results = query().union(sq1, sq2).orderBy(employee.id.asc()).fetch();
    for (Tuple result : results) {
      System.err.println(Collections.singletonList(result));
    }
  }

  @Test
  @ExcludeIn({FIREBIRD, TERADATA}) // The ORDER BY clause must contain only integer constants.
  @SuppressWarnings("unchecked")
  public void union_with_order() throws SQLException {
    SubQueryExpression<Integer> sq1 = query().from(employee).select(employee.id);
    SubQueryExpression<Integer> sq2 = query().from(employee).select(employee.id);
    List<Integer> list = query().union(sq1, sq2).orderBy(employee.id.asc()).fetch();
    assertThat(list).isNotEmpty();
  }

  @SuppressWarnings("unchecked")
  @Test
  @ExcludeIn(FIREBIRD)
  public void union_multi_column_projection_list() throws IOException {
    SubQueryExpression<Tuple> sq1 =
        query().from(employee).select(employee.id.max(), employee.id.max().subtract(1));
    SubQueryExpression<Tuple> sq2 =
        query().from(employee).select(employee.id.min(), employee.id.min().subtract(1));

    List<Tuple> list = query().union(sq1, sq2).list();
    assertThat(list).hasSize(2);
    assertThat(list.get(0) != null).isTrue();
    assertThat(list.get(1) != null).isTrue();
  }

  @SuppressWarnings("unchecked")
  @Test
  @ExcludeIn(FIREBIRD)
  public void union_multi_column_projection_iterate() throws IOException {
    SubQueryExpression<Tuple> sq1 =
        query().from(employee).select(employee.id.max(), employee.id.max().subtract(1));
    SubQueryExpression<Tuple> sq2 =
        query().from(employee).select(employee.id.min(), employee.id.min().subtract(1));

    try (CloseableIterator<Tuple> iterator = query().union(sq1, sq2).iterate()) {
      assertThat(iterator.hasNext()).isTrue();
      assertThat(iterator.next() != null).isTrue();
      assertThat(iterator.next() != null).isTrue();
      assertThat(iterator.hasNext()).isFalse();
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void union_single_column_projections_list() throws IOException {
    SubQueryExpression<Integer> sq1 = query().from(employee).select(employee.id.max());
    SubQueryExpression<Integer> sq2 = query().from(employee).select(employee.id.min());

    List<Integer> list = query().union(sq1, sq2).list();
    assertThat(list).hasSize(2);
    assertThat(list.get(0) != null).isTrue();
    assertThat(list.get(1) != null).isTrue();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void union_single_column_projections_iterate() throws IOException {
    SubQueryExpression<Integer> sq1 = query().from(employee).select(employee.id.max());
    SubQueryExpression<Integer> sq2 = query().from(employee).select(employee.id.min());

    try (CloseableIterator<Integer> iterator = query().union(sq1, sq2).iterate()) {
      assertThat(iterator.hasNext()).isTrue();
      assertThat(iterator.next() != null).isTrue();
      assertThat(iterator.next() != null).isTrue();
      assertThat(iterator.hasNext()).isFalse();
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void union_factoryExpression() {
    SubQueryExpression<Employee> sq1 =
        query().from(employee).select(Projections.constructor(Employee.class, employee.id));
    SubQueryExpression<Employee> sq2 =
        query().from(employee).select(Projections.constructor(Employee.class, employee.id));
    List<Employee> employees = query().union(sq1, sq2).list();
    for (Employee employee : employees) {
      assertThat(employee).isNotNull();
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  @ExcludeIn({DERBY, CUBRID})
  public void union_clone() {
    NumberPath<Integer> idAlias = Expressions.numberPath(Integer.class, "id");
    SubQueryExpression<Employee> sq1 =
        query()
            .from(employee)
            .select(Projections.constructor(Employee.class, employee.id.as(idAlias)));
    SubQueryExpression<Employee> sq2 =
        query()
            .from(employee)
            .select(Projections.constructor(Employee.class, employee.id.as(idAlias)));

    SQLQuery<?> query = query();
    query.union(sq1, sq2);
    assertThat(query.clone().select(idAlias).fetch()).hasSize(10);
  }
}
