/*====================================
id=1 schema=employees gen=employees

Ordering by one column

Select all data from Employee table. Order by id in descending order.
*/----------------------------------
select *
from employee
order by id desc

/*====================================
id=2 schema=employees gen=employees

Ordering by few columns

Select all data from Employee table. Order by department in descending order, last name, id.
*/----------------------------------
SELECT * FROM EMPLOYEE  order by dep_id desc, last_name, id

/*====================================
id=3 schema=employees gen=employees

Select specified columns

Select last name and date of birth for all employees. Order by last name in descending order and id.
*/----------------------------------
SELECT LAST_NAME , DATE_OF_BIRTH  FROM EMPLOYEE order by LAST_NAME desc, id

/*====================================
id=4 schema=employees gen=employees

Select distinct values

Select all distinct names. Order by name.
*/----------------------------------
SELECT distinct first_NAME FROM EMPLOYEE order by first_NAME

/*====================================
id=5 schema=employees gen=employees

Filter by one column

Select all employees who work in the third department. Order by first name, last name and id.
*/----------------------------------
SELECT * FROM EMPLOYEE where dep_id = 3 order by first_NAME, last_NAME, id

/*====================================
id=6 schema=employees gen=employees

Filter by few columns

Select all employees who work in the first department and have salary not less than 2000. Order by id.
*/----------------------------------
SELECT * FROM EMPLOYEE where dep_id = 1 and salary >= 2000 order by id

/*====================================
id=7 schema=employees gen=employees

Filter by one string column

Select all employees who's first names start with A letter. Order by id.
*/----------------------------------
SELECT * FROM EMPLOYEE where first_name like 'A%' order by id

/*====================================
id=8 schema=employees gen=employees

Filter by one date column

Select all employees who started working in between 2000 and 2010 years (inclusive). Order by id.
*/----------------------------------
SELECT * FROM EMPLOYEE where '2000-01-01' <= works_since and works_since < '2011-01-01' order by id

/*====================================
id=9 schema=employees gen=employees

Filter by one column 2

Select all employees who's first names end with 'a' letter.
Order by id.
*/----------------------------------
select *
from employee
where first_name like '%a'
order by id

/*====================================
id=10 schema=employees gen=employees

Filter by one column 3

Find all employees who's first names contain small 'a' letter.
Order by id.
*/----------------------------------
select *
from employee
where first_name like '%a%'
order by id

/*====================================
id=11 schema=employees gen=employees

Filter by complex condition (conjunction 1)

Find all employees who's first names contain small 'a' letter and last names contain small 'h' letter.
Order by id.
*/----------------------------------
select *
from employee
where first_name like '%a%' and last_name like '%h%'
order by id

/*====================================
id=12-1 schema=employees gen=employees

Length of a string

Find all employees who's length of last name is less than 5 characters.
Order by id.

Hint: you can find length of a string with help of 'length' function. For example: length(last_name)
*/----------------------------------
select *
from employee
where length(last_name) < 5
order by id

/*====================================
id=12-2 schema=employees gen=employees

Compare to a date

Find all employees who started working not earlier than in 2004 year.
Order by id.
*/----------------------------------
select *
from employee
where '2004-01-01' <= works_since
order by id

/*====================================
id=12 schema=employees gen=employees

Filter by complex condition (conjunction 2)

Find all employees who's first name contains small 'i' letter and length of last name is less than 5 characters
and who started working not earlier than in 2004 year.
Order by id.

Hint: you can find length of a string with help of 'length' function. For example: length(last_name)
*/----------------------------------
select *
from employee
where
    first_name like '%i%'
    and length(last_name) < 5
    and '2004-01-01' <= works_since
order by id

/*====================================
id=13 schema=employees gen=employees

Filter by complex condition (disjunction 1)

Find all employees who's first name contains small 'y' letter or small 'w' letter.
Order by id.

*/----------------------------------
select *
from employee
where
    first_name like '%y%'
    or first_name like '%w%'
order by id

/*====================================
id=14 schema=employees gen=employees

Filter by complex condition (disjunction 2)

Find all employees who's last name ends with one of the following characters: l,t,y,e.
Order by id.

*/----------------------------------
select *
from employee
where
    last_name like '%l'
    or last_name like '%t'
    or last_name like '%y'
    or last_name like '%e'
order by id

/*====================================
id=15 schema=employees gen=employees

Filter by complex condition (disjunction 3)

Find all employees who's first name contains 4th letter of English alphabet.
Order by id.

*/----------------------------------
select *
from employee
where first_name like '%d%' or first_name like 'D%'
order by id

/*====================================
id=16 schema=employees gen=employees

Filter by complex condition (disjunction and conjunction)

Among all employees of the first and fourth departments, find those whose first name starts with a vowel.
Order by id.

*/----------------------------------
select *
from employee
where
    (dep_id = 1 or dep_id=4)
    and (first_name like 'A%'
                or first_name like 'E%'
                or first_name like 'I%'
                or first_name like 'O%'
                or first_name like 'U%'
                or first_name like 'Y%')
order by id

