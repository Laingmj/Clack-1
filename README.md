# Clack-1

Michael Laing
Ngaio Hawkins

Question:  In your test classes, what happens if you provide a negative value for a port
number, or a null value for a user? How do you think you can fix these issues?

A negative port number would simply result in negative hashcodes. If you wanted to avoid this, you could calculate the hashcode with an absolute value orr change the constructor to use absolute value for port numbers.
