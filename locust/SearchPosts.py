import json
from locust import HttpUser, task, between
import random



class SearchPosts(HttpUser):
  wait_time = between(1, 2)

  def on_start(self):
    self.client.post("/users/sign-in", json = {
      "userId" : "tester",
      "password" : "123"
    })

  @task
  def search_posts(self):
    sortStatus = random.choice(["CATEGORIES", "NEWEST", "OLDEST", "HIGH_PRICE", "LOW_PRICE", "GRADE"])
    categoryId = random.randint(1, 10)
    name = '테스트 게시글'.join(str(random.randint(1, 10000)))
    headers = {"Content-Type" : "application/json"}
    data = {"sortStatus" : sortStatus, "categoryId" : categoryId, "name" : name}

    self.client.post("/search/cache", json = data, headers = headers)

