select *
from employee
where length(last_name) < 5
order by id