/*====================================
id=2019-06-02--exp-1 schema=employees gen=employees

Expressions: true and null.

You already know that, for example, "true and false" evaluates to false, or "false or true" evaluates to true.
But what about null values in expressions? Try to find a way how to check what a result of "true and null" is.
Hint: in order to have spaces in column name you have to surround column name with backticks.
Backtick key is usually placed to the left of the "1" key.
For example: select something as `name with spaces` from table.
*/----------------------------------

select true and null `true and null`
from employee
where id = 150

/*====================================
id=2019-06-02--exp-2 schema=employees gen=employees

Expressions: true or null.

What does "true or null" evaluate to?
*/----------------------------------

select true or null `true or null`
from employee
where id = 150

/*====================================
id=2019-06-02--exp-3 schema=none gen=none

dual table.

In previous exercises you have to use some condition in "where" clause to get only one row in a result.
There is a table with name "dual" specially for this purpose. Explore what is inside of this table.
*/----------------------------------

select * from dual

/*====================================
id=2019-06-02--exp-4 schema=none gen=none

Expressions: substring

What does "substring('ABCDEFGHIJKL',2,5)" evaluates to?
*/----------------------------------

select substring('ABCDEFGHIJKL',2,5) `substring('ABCDEFGHIJKL',2,5)` from dual

/*====================================
id=2019-06-02--exp-5 schema=none gen=none

Expressions: substring 2

What does "substring('ABCDEFGHIJKL',4,3)" evaluates to?
*/----------------------------------

select substring('ABCDEFGHIJKL',4,3) `substring('ABCDEFGHIJKL',4,3)` from dual

/*====================================
id=2019-06-02--exp-6 schema=none gen=none

Expressions: substring 3

What does "substring('ABCDEFGHIJKL',5,1)" evaluates to?  Try to understand how substring function works.
*/----------------------------------

select substring('ABCDEFGHIJKL',5,1) `substring('ABCDEFGHIJKL',5,1)` from dual

/*====================================
id=2019-06-02--exp-7 schema=none gen=none

Expressions: substring 4

How to get the first letter of the string 'ABCDEFGHIJKL' using substring function?
*/----------------------------------

select substring('ABCDEFGHIJKL',1,1) first_letter from dual

/*====================================
id=2019-06-02-8 schema=employees gen=employees

Grouping by expression.

Group employees by the first letter of their last name and count how much employees are in each such group.
Order by the letter in descending order.

*/----------------------------------

select substring(last_name,1,1) letter, count(id) number_of_employees
from employee
group by substring(last_name,1,1)
order by substring(last_name,1,1) desc

/*====================================
id=2019-06-02-9 schema=employees gen=employees

Ordering by expression.

As in the previous exercise, group employees by the first letter of their last name and count how much employees are in each such group.
But now, order by number or employees to better see which letter "wins" (i.e. has more employees).


*/----------------------------------

select substring(last_name,1,1) letter, count(id) number_of_employees
from employee
group by substring(last_name,1,1)
order by count(id) desc

