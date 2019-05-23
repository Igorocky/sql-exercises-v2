/*====================================
id=2019-05-23--1 schema=employees gen=employees

Modification of output 1

Select amount of tax each employee has to pay. Tax is 35%. order by id.
*/----------------------------------
select id, FIRST_NAME, LAST_NAME, SALARY, SALARY*0.35 TAX
from employee
order by id

/*====================================
id=2019-05-23--2 schema=employees gen=employees

Modification of output 2

For each employee of the 3rd department, select how much money they will have after tax deduction. Tax is 35%. order by id.
*/----------------------------------
select id, dep_id, FIRST_NAME, LAST_NAME, SALARY, SALARY*0.65 AFTER_TAX_DEDUCTION
from employee
where dep_id=3
order by id

/*====================================
id=2019-05-23--3 schema=employees gen=employees

Modification of output 3

For each employee of the first and fourth departments, select their id, first and last names,
and first name surrounded by '*' symbols. order by id.
*/----------------------------------
select id, dep_id, FIRST_NAME, LAST_NAME, '***'||FIRST_NAME||'***' FIRST_NAME_CHANGED
from employee
where dep_id=1 or dep_id=4
order by id

