/*====================================
id=2019-06-02--1 schema=employees gen=employees

Aggregate over all table

Find maximum salary over all employees.
*/----------------------------------
select max(salary) maximum_salary
from employee

/*====================================
id=2019-06-02--2 schema=employees gen=employees

Aggregate over groups

Find maximum and minimum salary for each department. Order by department.
*/----------------------------------
select dep_id, min(salary) minimum_salary, max(salary) maximum_salary
from employee
group by dep_id
order by dep_id

/*====================================
id=2019-06-02--3 schema=employees gen=employees

Aggregate over calculation in groups

Find maximum tax for each department. Tax is 30%. Order by department.
*/----------------------------------
select dep_id, min(salary*0.3) minimum_tax, max(salary*0.3) maximum_tax
from employee
group by dep_id
order by dep_id

/*====================================
id=2019-06-02--4 schema=employees gen=employees

Restrict amount of groups.

Find minimum tax for the second, fourth and fifth departments. Tax is 30%. Order by department.
*/----------------------------------
select dep_id, min(salary*0.3) minimum_tax
from employee
where dep_id in (2,4,5)
group by dep_id
order by dep_id

/*====================================
id=2019-06-02--5 schema=employees gen=employees

Counting.

Find number of employees in each department. You may use count() aggregate function. Order by department.
*/----------------------------------
select dep_id, count(id) number_of_employees
from employee
group by dep_id
order by dep_id

/*====================================
id=2019-06-02-6 schema=employees gen=employees-with-nulls

null value.

In databases there is a special value null. It indicates that actually there is no value.
When a new employee comes to a company but it is not decided  yet what department he or she will be working at,
a null value in dep_id may be used to indicate such situation. Find all employees who are not assigned any department.
Order by id.
*/----------------------------------

select *
from employee
where dep_id is null

/*====================================
id=2019-06-02-7 schema=employees gen=employees-with-nulls

Counting 2.

How much there are employees who are not assigned any department?

*/----------------------------------

select count(id) number_of_employees_without_department
from employee
where dep_id is null

