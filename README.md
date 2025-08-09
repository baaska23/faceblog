# FaceBlog

## Abstract
FaceBlog is a full-stack web application that enables users to publish, edit, and manage articles while providing search functionality for readers. The project is built using VueJS for the frontend and Spring Boot for the backend, creating a modern and responsive blogging platform.

## Requirements
The application provides comprehensive article management capabilities with user authentication and role-based access control. Users can perform the following operations:

- Add, update, and delete articles
- Comment on articles
- Search articles by title
- Secure login with role-based access control (RBAC) for editing and deletion operations

## Properties
Each article contains the following attributes:

- **id**: A unique identifier for the article
- **title**: A concise header describing the article
- **content**: The main body content of the article
- **published_at**: The date and time when the article was created
- **updated_at**: The date and time when the article was last modified

Each comment contains the following attributes:

- **id**: A unique identifier for the comment
- **commenting_user**: Name of user who comments
- **article_id**: The id of corresponding article
- **comment**: The comment on corresponding article

## API Reference

#### Get all articles
```http
GET /api/articles
```

#### Get article by id
```http
GET /api/articles/${id}
```

#### Edit article by id
```http
PUT /api/articles/${id}
```

#### Delete article by id
```http
DELETE /api/articles/${id}
```

#### Get all users
```http
GET /api/auth/users
```

#### Send login request
```http
POST /api/auth/login
```

#### Send comment
```http
POST /api/comments/{articleId}
```

#### Get comments by article
```http
GET /api/comments-per-article/{id}
```

## Features
- Full-stack development with modern technologies
- Autocomplete search functionality
- Session storage for user state management
- Role-based access control (RBAC)
- Commenting logic
- Responsive user interface

## Tech Stack
- **Frontend**: VueJS
- **Backend**: Spring Boot
- **Architecture**: MVC (Model-View-Controller)

## Screenshots
https://postimg.cc/gallery/CLjfsR5

## Lessons Learned
Through this project, I gained valuable experience in utilizing Spring Boot to develop backend services and API endpoints. I learned how to integrate databases with both frontend and backend components, and I also applied MVC architecture pattern to this project. Overall, the project enhanced my skills in full-stack development and user authentication systems.

## Authors
- [@kai_uuganbadrakh](https://github.com/baaska23)

## Acknowledgements
- [Project idea](https://roadmap.sh/projects/personal-blog)

## References
- [Spring Boot endpoints documentation](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)
- [Session storage](https://developer.mozilla.org/en-US/docs/Web/API/Window/sessionStorage)
- [VueJS documentation](https://vuejs.org/guide/introduction.html)
