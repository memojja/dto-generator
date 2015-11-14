/**
 * The MIT License
 *
 * Copyright (c) 2015, Sebastian Sdorra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.sdorra.dto;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
@GenerateDTO("groups")
public class Group
{
  @DTOField(id = true)
  private String name;
  private String description;
  private List<User> users;

  Group()
  {
  }

  public Group(String name, String description, User... users)
  {
    this(name, description, Arrays.asList(users));
  }
  
  public Group(String name, String description, List<User> users)
  {
    this.name = name;
    this.description = description;
    this.users = users;
  }

  public void setUsers(List<User> users)
  {
    this.users = users;
  }
  
  public String getName()
  {
    return name;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getDescription()
  {
    return description;
  }

  public List<User> getUsers()
  {
    return users;
  }

}
