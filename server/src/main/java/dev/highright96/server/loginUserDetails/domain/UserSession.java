package dev.highright96.server.loginUserDetails.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSession {

    private String username;
    private List<SessionInfo> sessions;

    public int getCount() {
        return sessions.size();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SessionInfo {
        private String sessionId;
        private Date time;
    }

}
