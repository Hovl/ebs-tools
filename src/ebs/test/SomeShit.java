package ebs.test;

import java.util.List;
import java.util.Map;

/**
 * Created by Aleksey Dubov.
 * Date: 2011-02-10
 * Time: 00:44
 * Copyright (c) 2010
 */
public class SomeShit {
	private String name;
	private String type;
	private Integer status;
	private Boolean active;
	private List<String> list;
	private Map<String, String> data;

	public SomeShit() {
	}

	public SomeShit(String name, String type, Integer status, Boolean active, List<String> list,
					Map<String, String> data)
	{
		this.name = name;
		this.type = type;
		this.status = status;
		this.active = active;
		this.list = list;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SomeShit someShit = (SomeShit) o;

		if (active != null ? !active.equals(someShit.active) : someShit.active != null) return false;
		if (data != null ? !data.equals(someShit.data) : someShit.data != null) return false;
		if (list != null ? !list.equals(someShit.list) : someShit.list != null) return false;
		if (name != null ? !name.equals(someShit.name) : someShit.name != null) return false;
		if (status != null ? !status.equals(someShit.status) : someShit.status != null) return false;
		if (type != null ? !type.equals(someShit.type) : someShit.type != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (active != null ? active.hashCode() : 0);
		result = 31 * result + (list != null ? list.hashCode() : 0);
		result = 31 * result + (data != null ? data.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("SomeShit");
		sb.append("{name='").append(name).append('\'');
		sb.append(", type='").append(type).append('\'');
		sb.append(", status=").append(status);
		sb.append(", active=").append(active);
		sb.append(", list=").append(list);
		sb.append(", data=").append(data);
		sb.append('}');
		return sb.toString();
	}


}
