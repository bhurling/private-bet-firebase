## Bets

A bet can be in the following states:

* A: Open (Deadline not reached, no decision was made)
* B: Deadline passed but no decision was made
* C: Signed in user claims the win (but other party did not approve yet)
* D: Other party claims the win (but signed in user did not approve yet)
* E: Winner was agreed upon (but stakes were not settled yet)
* F: Settled

The bets feed should sort bets as follows:

* Bets for which the other party claims victory (D)
  Actions: Agree
* Bets for which the deadline has passed (B)
  Actions: Claim victory, give credit
* Bets for which the signed in user claims victory (C)
  Actions: N/A
* All stakes that need to be paid (E). The statement is not important
  Actions: Settle
* Open (A), sorted
  Actions: N/A

## Friends and invitations

### Use Cases

* Query: Show empty state if there are no incoming or confirmed invitations
* Query: List all users who have sent me an invitation
* Query: List all users that I have confirmed invitations with
* Query: List all users that I have sent invitations to
* Mutation: Send invitation to another user
* Mutation: Accept invitation from another user
* Mutation: Reject invitation from another user