<p align="center">
<img src="https://carreiras.pucminas.br/med/2022/09/logo_04.png" height="200" width="200">
</p>

# Compass Blog API Processor

## Objective
Create an application that asynchronously fetches posts from an external API, enriches them with comment data, and keeps a log of processing updates. The client will then be able to search for posts and the history of states through the API immediately.

## Technical Requirements
- The app must run on port 8080.
- The database must be an embedded H2 database.
- The Spring configuration `spring.jpa.hibernate.ddl-auto` should be set to `create-drop`.
- If there is a message broker, it should be embedded as well.

## Evaluation
The app will be subjected to a bombardment of requests simulating a high volume of processing, and it is expected that the responses and processing will be as close to real-time as possible. The evaluation will be based primarily on the objective and technical requirements. Additionally, the quality of the code, chosen architecture, and the resilience of the solution will be assessed.

## External Data Source
**URL**: https://jsonplaceholder.typicode.com

## States of Posts
- **CREATED**: Initial state of a new post.
- **POST_FIND**: Indicates that the app is searching for basic post data.
- **POST_OK**: Indicates that the basic post data is already available.
- **COMMENTS_FIND**: Indicates that the app is searching for post comments.
- **COMMENTS_OK**: Indicates that the post comments are already available.
- **ENABLED**: Indicates that the post has been successfully processed and is enabled.
- **DISABLED**: Indicates that the post is disabled, either due to a processing failure or by user decision.
- **UPDATING**: Indicates that the post needs to be reprocessed.
- **FAILED**: Indicates a processing error.

The states of posts can only transition to other states as shown in the image below:

<p align="center">
<img src="https://voltaic-fear-b2d.notion.site/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fb82b5877-5097-4cdb-8257-deee77d011f7%2FImage1.png?table=block&id=3f195f33-f65d-4ec9-88b2-155df8f4dcf4&spaceId=a0026fc9-110f-43d0-ab98-8544b5b60197&width=1410&userId=&cache=v2" height="562" width="604">
</p>


## API / Features

### Process Post
- **Description**: Processes a post.
- **Method**: POST
- **Path**: /posts/{postId}
- **Requirements**:
    - postId must be a number between 1 and 100.
    - Existing postId should not be accepted.

### Disable Post
- **Description**: Disables a post that is in the "ENABLED" state.
- **Method**: DELETE
- **Path**: /posts/{postId}
- **Requirements**:
    - postId must be a number between 1 and 100.
    - postId should be in the "ENABLED" state.

### Reprocess Post
- **Description**: Reprocesses a post that is in the "ENABLED" or "DISABLED" state.
- **Method**: PUT
- **Path**: /posts/{postId}
- **Requirements**:
    - postId must be a number between 1 and 100.
    - postId should be in the "ENABLED" or "DISABLED" state.

### Query Posts
- **Description**: Provides a list of posts.
- **Method**: GET
- **Path**: /posts
- **Response**:
<p align="center">
<img src="https://voltaic-fear-b2d.notion.site/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fe6c2fec6-e9ff-41e2-bac4-41a001f157f9%2FImage2.png?table=block&id=03b26650-0f9a-48a5-b6fc-1f06b7776e38&spaceId=a0026fc9-110f-43d0-ab98-8544b5b60197&width=1210&userId=&cache=v2" height="397" width="604">
</p>

    - `title`: Can be null or empty depending on the state.
    - `body`: Can be null or empty depending on the state.
    - `comments`: Can be null or empty depending on the state.
    - `history`: Cannot be null or empty; it must always have a value.

## Pagination
 
To make a request in method GET, you can use pagination setting the endpoint like the following example:

- **Method**: GET
- **Path**: /posts?pageNo=0&pageSize=2

Feel free to explore the API endpoints and start using it for your Compass Blog API Processor management needs!

Happy coding!