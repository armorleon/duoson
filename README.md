# jsonparser

TODO List:

a. Differentiate real token and fake token, E.g. for case { “abc” : “1{}2” }    { } on two sides are real tokens, but {} between 1 and 2 are fake tokens and should be ignored and considered as normal string only.

b. Support Unicode to handle multi-languages.

c. TokenReader should be extended to support byte stream instead of just String input.

d. Can we do ORM here? Similar to Hibernate and convert json string to meaningful objects instead of just java Object.

e. Make the code more beautiful and object oritented, like convert the big switch-case to rule-action models
