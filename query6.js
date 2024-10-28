// Query 6
// Find the average friend count per user.
// Return a decimal value as the average user friend count of all users in the users collection.

function find_average_friendcount(dbname) {
    db = db.getSiblingDB(dbname);

    // TODO: calculate the average friend count
    let numFriends = 0;
    let numUsers = 0;

    db.users.find().forEach(function(user) {
        numFriends = numFriends + user.friends.length;
        numUsers = numUsers + 1;
    });

    let average = numFriends / numUsers;

    return average;
}
