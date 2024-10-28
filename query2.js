// Query 2
// Unwind friends and create a collection called 'flat_users' where each document has the following schema:
// {
//   user_id:xxx
//   friends:xxx
// }
// Return nothing.

function unwind_friends(dbname) {
    db = db.getSiblingDB(dbname);

    // TODO: unwind friends
    db.users.find().forEach(function(user) {
        user.friends.forEach(function(id) {
            db.flat_users.insert ({
                user_id: user.user_id,
                friends: id
            });
        });
    });
}
