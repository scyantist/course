# Homework 0

## Logistics
**Due Date: TBA**

## Assignment

If you haven't already, please go to the [EECS inst sign up website](http://inst.eecs.berkeley.edu/webacct) and sign up for a course account for CS 186. Once you've done that, please go to the [course sign-up website](http://register.cs186berkeley.net) and fill out the sign up information, including your inst account information. Once that's done, you should get an automated email from Github saying you've been added to the `berkeley-cs186` organization. Within that organization, you'll be added to a Github team (just a way for us set up access rights correctly) corresponding to your inst login (e.g., `aaa` if your inst login was `aaa`) and a repository of the same name. You'll use this repo to submit all of your assignments.

Once you have this repo set up, you should add `https://github.com/berkeley-cs186/course` (or `git@github.com:berkeley-cs186/course` if you have SSH keys enabled) as a remote called `course`. Run `git pull course master` to get the starter code for this assignment -- in this case, just a folder called `hw0` with this README inside. In the future, there will be starter code for other assignments. Again, to be clear, you should now have two remotes: `origin` which you will push to (specifically to the `submit` branch which is covered later) and `course` which you will pull from. 

The last thing you need to do is create a file called `hw0.txt` within `hw0/` (e.g., `touch hw0/hw0.txt`), create a new commit, and push to the **submission branch as well as master**. In general, you will probably want to keep all your assignments up-to-date in `master`, but the branches that we are going to use to grade your assignments will be named `submit/hw*`. In this case, you should push to **both** `master` **and** to `submit/hw0` or else Github will delete your master branch. Push to master with `git push origin master`. Push to the submission branch with `git push origin master:submit/hw0`. (Alternatively, you can create a new branch called `submit/hw0` locally and then run `git push origin submit/hw0`.) Once you've submitted the assignment, you should get a confirmation email from the autograder saying that you've successfully submitted the assignment.



## tl;dr
1. Sign up on the [EECS inst sign up website](http://inst.eecs.berkeley.edu/webacct)
2. Sign up on the [course sign-up website](http://register.cs186berkeley.net)
3. Wait till you get added to the `berkeley-cs186` org
4. Clone your repo.
5. Add the `course` repo as a remote and pull
6. Create a file called `hw0.txt` in the `hw0` directory.
7. Commit and push to **both** `master` **and** `submit/hw0`
8. Read the next section because it's important.

## Future Assignments

For future assignments, you will submit assignments to `submit/hw*`. For certain assignments (usually projects), autograders will be provided. For those assignments, if you don't want to submit and just want to get autograder feedback, you will use the `ag/hw*` branch.