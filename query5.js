// Query 5
// Find the oldest friend for each user who has a friend. For simplicity,
// use only year of birth to determine age, if there is a tie, use the
// one with smallest user_id. You may find query 2 and query 3 helpful.
// You can create selections if you want. Do not modify users collection.
// Return a javascript object : key is the user_id and the value is the oldest_friend id.
// You should return something like this (order does not matter):
// {user1:userx1, user2:userx2, user3:userx3,...}

function oldest_friend(dbname) {
    db = db.getSiblingDB(dbname);

    let results = {};

    db.users.find ({ friends: { $exists: true, $ne: [] }}).forEach(function(user) {
        let oldest = null;
        user.friends.forEach(function(id) {
            let myFriend = db.users.findOne({ user_id: id});

            if(!myFriend) return;

            if(oldest === null || myFriend.YOB < oldest.YOB || 
                (myFriend.YOB === oldest.YOB && myFriend.user_id < oldest.user_id)) {
                    oldest = myFriend;
            }
        });
        
        if (oldest != null) {
            results[user.user_id] = oldest.user_id;
        }
    });
    // TODO: implement oldest friends

    return results;
}
