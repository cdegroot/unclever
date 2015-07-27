[![Build Status](https://travis-ci.org/cdegroot/unclever.svg)](https://travis-ci.org/cdegroot/unclever)
 [![Gitter](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/cdegroot/unclever)

Unclever is currently under design, check [the design spec](https://github.com/cdegroot/unclever/blob/master/src/test/scala/com/evrl/unclever/BasicUsage.scala) and join [the chat](https://gitter.im/cdegroot/unclever) to participate.

# unclever
The stupid way to hook Scala code up to databases.

Antonym of Slick ;-)

## Rant
I'm fed up with tossing out ORM tools because they don't work. Frankly, the only usable thing ever to come out of Spring was JDBC Templates, which is some nice and helpful sugar for talking to JDBC. This library aims to do the same for Scala.

Once, I worked at a project where we had an open source ORM, in the best OO language on the planet, with the author part of the project. His credentials? He worked on one of the most successful commercial ORMs on the planet before.

We ended up tossing it out. 

## You're out of your mind!
You are absolutely right there, but that's besides the point. 

What *is* the point, is that if you have to bridge a gap, be it between objects and relational databases or two machines across a network, explit is better. Magic is worse. All ORMs work by magic. If you don't believe me, read their source code.

Your schema is way more stable than you suspect. And updating three methods is actually not a lot of work if you add a column. 

And the port from MySQL to Postgres? You need to test, profile, and whatnot anyway. Fixing the ten query compilation errors is a tiny drop. 

## I disagree!
Fine. This is Open Source, I'm not ramming it down your throat, so please move on and ignore me. The code will still be there next year, when you want it :P

## So can I use this code to run my nuclear reactor?
Of course. Don't sue me, however, if stuff blows up. 

This is a work-in-progress. Having said that, Unclever is mostly complete, specifically patches to do the following won't be accepted:
- Anything with metadata (know thy schema);
- Anything with stored procedures (they're eee-vul);
- Pretty much anything over basic CRUD stuff. This is a minimalistic, understandable, predictable library. Not a framework.
So, before submitting a pull request, ping me whether I actually want new code in. Bugfixes are always welcome, though :)

Having said that, current list of TODOs:
- Complete DbValue/ParamValue (the current set is copmlete but opiniated);
- Support for returning the primary key on an insert.

About DbValue and ParamValue's "complete but opiniated" state: In my opinion (which counts here, because 
this is my code), boolean/char/int/long should be the only types to use (I don't work in scientific computing, 
so floats are never needed. If you need money types, don't use floats. Use a long representing the smallest
denomination, like dollar cents. Sometimes, when you're not overdoing it, it's convenient to have a blob in 
the database, but make it a binary blob so you control charset conversions etcetera, and bind using byte 
arrays instead of streams because it's simpler and forces you to start scratching your head before you
start streaming gigabyte blobs into an RDBMS where you really want some K/V storage. So these types I 
provide, the rest I don't basically because I have an opinion and I'm lazy and given that these
are typeclasses, it's trivial to extend them.
