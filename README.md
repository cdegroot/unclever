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

## Code? Which code?
Sharp! This is total vapourware until the next PagerDuty hackday :-)
