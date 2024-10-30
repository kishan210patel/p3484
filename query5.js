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

  // TODO: implement oldest friends
  db.users.find().forEach(function (user) {
    let min_id = 9999999;
    let min_year = 99999;
    let old = -1;

    if (user.friends) {
      db.users
        .find({ user_id: { $in: user.friends } })
        .forEach(function (person) {
          if (
            person.YOB < min_year ||
            (person.YOB === min_year && person.user_id < min_id)
          ) {
            old = person.user_id;
            min_id = person.user_id;
            min_year = person.YOB;
          }
        });
    }

    db.users.find({ friends: user.user_id }).forEach(function(person) {
      if (
        person.YOB < min_year ||
        (person.YOB === min_year && person.user_id < min_id)
      ) {
        old = person.user_id;
        min_id = person.user_id;
        min_year = person.YOB;
      }
    });

    if (old !== -1) {
      results[user.user_id] = old;
    }

  });
  
  return results;
}
