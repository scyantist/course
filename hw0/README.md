# Homework 0

In this assignment, you will set up the tools needed to submit assignments in
CS 186.

This assignment is due **Tuesday, January 24** at **11:59 PM**.

## Instructions (brief)
0. If you don't have one yet, create a GitHub account.
1. Set up your EECS instructional account
[here](http://inst.eecs.berkeley.edu/webacct).
2. Fill out the form [here](http://cs186-reg.herokuapp.com/) to register your
account with our course. Note that you should do this **as soon as
possible**; **you will not be able to submit any assignments until this step
is complete**.
3. Complete setting up your GitHub repository after receiving access over e-mail.
4. Create the file `hw0.txt` in the subdirectory `hw0` and commit it to your
repository.
5. Submit this assignment by pushing your finished commit to the branch
`submit/hw0`.

# `git` and GitHub

`git` is a *version control* system, helping you track different versions of
your code, synchronize them across different machines, and collaborate with
others. [GitHub](https://github.com) is a site which supports this system,
hosting it as a service.

We will be using both `git` and GitHub to submit assignments in this course. If
you don't know much about `git`, we *strongly recommend* you to familiarize
yourself with this system; you'll be spending a lot of time with it!

There are many guides to using `git` online -
[here](http://git-scm.com/book/en/v1/Getting-Started) is a great one to read.
Feel free to ask us other questions during our office hours.

## 0. Creating and registering your GitHub account

If you don't yet have a GitHub account, create one by following the
instructions [here](https://help.github.com/articles/set-up-git/).


## 1. Creating your EECS instructional account

Go to [EECS Instructional WebAcct Login](http://inst.eecs.berkeley.edu/webacct)
and sign up for a course account for CS 186. You'll have to enter your
Berkeley credentials, then click "Get a new account" under Account Services.

## 2. Register your account with the course

Register your information by filling out this
[form](http://cs186-reg.herokuapp.com/). Be sure to put down your *3-letter* inst
account login.

Within an hour, you will receive an e-mail with a link to your personal GitHub
course repository from the `berkeley-cs186` organization. Accept the GitHub
invitation to continue. **If you do not receive such an e-mail within an hour,
double-check that you filled out the form correctly**.

# Submitting Assignments in CS 186

All assignments in CS 186 will be submitted by turning code in through GitHub
by pushing the relevant commit to a specific branch, unless we say otherwise.

You have access to two repositories in the `berkeley-cs186` GitHub
organization:

1. [A course repository which contains public assignment
information](https://github.com/berkeley-cs186/course) which you can *only*
read from. We will post assignments **and updates** here. Please check our
course site and Piazza to keep up-to-date on changes to assignments.

2. A personal repository whose name corresponds to the last three characters in
your `inst.eecs.berkeley.edu` login which only you can see and which you can
read and write to. (e.g. If your login is `cs186-xyz`, then this repository
will be called `xyz`.) You will be using this repository to submit your
assignments. You must keep the contents of this repository secure: **remember
that we expect you to adhere to course policy regarding collaboration and
academic honesty**.

Each assignment resides in a different top-level directory. Each assignment has
a `README` which contains instructions, as well as the necessary files for that
particular assignment. (For instance, the instructions you are reading right
now.)

Your personal repository currently should be empty except for the `README.md`
file at its root.

## Setting up your repositories

For these instructions, replace `xyz` with your three-letter account suffix.

You should first set up a local repository.

    $ cd ~/directory-of-your-choice/

Clone your (private) personal repository.

    $ git clone https://github.com/berkeley-cs186/xyz.git

Enter the repository's directory, add the remote tracking the (public) course
repository URL, and download (and merge) its contents.

    $ cd xyz/
    $ git remote add course https://github.com/berkeley-cs186/course.git
    $ git pull course master

Push these contents to your (private) personal repository.

    $ git push origin master

Please keep in mind that you will have *two* remote repositories. The `course`
remote points to the public `course` repository, which has the homework and
project skeletons we release. You'll `pull` from the `course` remote. The
`origin` remote points to your private personal repository `xyz`, the place
where you'll `push` your assignments. Make sure you `pull` from and `push` to
the right remotes by specifying the correct name when you run your git
commands.

## Receiving new assignments and assignment updates

We will release new assignments by registering them as commits in the official
course repository. From time to time, it may also be necessary to `pull`
updates to our assignments (even though we try to release them as "perfectly"
as possible the first time). Assuming you followed the previous steps, you can
simply run the following command to receive new assignments and/or updates:

    $ git pull course master

## Running the autograder

On some assignments we will provide you with an autograding service, which may
give you some feedback on how you're doing. To run the autograder on this
assignment, push a branch called `ag/hw0` to your repository.

    $ git push origin master:ag/hw0

Our machines will e-mail you the results of the autograder within an hour. If
you do not receive a response after an hour, please *first* double-check that
all your files are in the right place, that you pushed a commit to `ag/hw0`,
*and* that our e-mail message is not in your junk mail folder.

Keep in mind that you must still submit this assignment by pushing a branch
called `submit/hw0`: **we will not grade submissions to the autograder!**

## Submitting assignments

To submit an assignment, push a branch named `submit/<assignment-name>` to
your personal repository, where `<assignment-name>` is the name of your
assignment. For example:

    $ git push origin master:submit/hw0

Keep in mind that you must do this to make sure you've submitted your
assignment correctly. You can confirm that you've submitted correctly checking
that the branch `submit/<assignment-name>` exists on GitHub.

We will grade the last commit you've pushed to `submit/<assignment-name>`.
Additionally, the timestamp of this commit is what we will use to determine
lateness. (Submit well before the deadline to avoid penalties caused by your
Internet connection!)

## 4. An example: completing this assignment

If you haven't already, first clone your personal repository from GitHub:

    $ cd ~/directory-of-your-choice/
    $ git clone https://github.com/berkeley-cs186/xyz.git
    $ cd xyz/
    $ git remote add course https://github.com/berkeley-cs186/course.git
    $ git pull course master

Move into the `hw0` directory of your repository and create the file `hw0.txt`.
Register this change with a commit and push it into GitHub's servers:

    $ cd ~/directory-of-your-choice/xyz/hw0/
    $ touch hw0.txt
    $ git add hw0.txt
    $ git commit -m 'created homework file'
    $ git push origin master

Not for Homework 0, but in future assignments you can run our autograder:

    $ git push origin master:ag/assignment

Occasionally we might release an update to assignment files (make sure you add
the course remote as detailed above first):

    $ git pull course master

Finally, when you're satisfied with your submission, submit it:

    $ git push origin master
    $ git push origin master:submit/hw0

Once you've submitted the assignment, you should get a confirmation e-mail from
the autograder saying that you've successfully submitted the assignment.

...and you're done!
