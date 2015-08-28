[![Build Status](https://travis-ci.org/cdegroot/unclever.svg)](https://travis-ci.org/cdegroot/unclever)
 [![Gitter](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/cdegroot/unclever)


# unclever
The stupid way to hook Scala code up to databases. A RantWare™ production by Cees de Groot

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

## How does it work? 
You can find usage examples in [the design spec](https://github.com/cdegroot/unclever/blob/master/src/test/scala/com/evrl/unclever/BasicUsage.scala). I'm lazy and documentation gets out of date, which is why I keep the design spec as a bunch of (ignored) tests - at least as long as they'll compile, they'll have some relevance to the actual code.


## So can I use this code to run my nuclear reactor?
Of course. Don't sue me, however, if stuff blows up. 

This is a work-in-progress. Having said that, Unclever is mostly complete, specifically patches to do the following won't be accepted:

- Anything with metadata (know thy schema);
- Anything with stored procedures (they're eee-vul);
- Probably anything added in JDBC 2.0;
- Pretty much anything over basic CRUD stuff. This is a minimalistic, understandable, predictable library. Not a framework.

So, before submitting a pull request, ping me whether I actually want new code in. Bugfixes are always welcome, though :)

## I'm sold. How can I use it?

The [Apache 2](http://www.apache.org/licenses/LICENSE-2.0) License applies. Have fun.

Publishing small libraries is too much work. I recommend you use [JitPack](https://jitpack.io/#cdegroot/unclever) to make both our lives more pleasant.
