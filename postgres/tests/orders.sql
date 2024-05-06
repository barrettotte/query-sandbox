-- test queries related to orders

select count(*), status
from orders
group by orders;
