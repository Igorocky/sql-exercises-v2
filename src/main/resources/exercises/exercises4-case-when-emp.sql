/*====================================
id=2019-05-30--1 schema=employees gen=employees

Conditional calculation in select

There are few tax groups.
The first group is for those who is under 25 years and has salary less than 2000. Tax for the first group is 18%.
The second group is for those who is not in the first group and has salary less then 4000. Tax for the second group is 23%.
The third group includes all other employees. Tax for the third group is 28%.
Calculate tax for each employee.
order by id.
*/----------------------------------
select id, first_name, last_name,
case when DATEDIFF('YEAR', DATE_OF_BIRTH, CURRENT_DATE()) < 25
from employee
order by id

