package io.gazetteer.osm.postgis;

import static io.gazetteer.common.postgis.GeometryUtils.toGeometry;

import io.gazetteer.osm.model.StoreException;
import io.gazetteer.osm.model.StoreReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

public class PostgisCoordinateStore implements StoreReader<Long, Coordinate> {

  private static final String SELECT =
      "SELECT st_asbinary(ST_Transform(geom, 4326)) FROM osm_nodes WHERE id = ?";

  private static final String SELECT_IN =
      "SELECT id, st_asbinary(ST_Transform(geom, 4326)) FROM osm_nodes WHERE id = ANY (?)";

  private static final String COPY =
      "COPY osm_nodes (id, version, uid, timestamp, changeset, tags, geom) FROM STDIN BINARY";

  private final DataSource dataSource;

  public PostgisCoordinateStore(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Coordinate get(Long id) {
    try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT)) {
      statement.setLong(1, id);
      ResultSet result = statement.executeQuery();
      if (result.next()) {
        Point point = (Point) toGeometry(result.getBytes(6));
        return point.getCoordinate();
      } else {
        throw new IllegalArgumentException();
      }
    } catch (SQLException e) {
      throw new StoreException(e);
    }
  }

  @Override
  public List<Coordinate> getAll(List<Long> keys) {
    try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_IN)) {
      statement.setArray(1, connection.createArrayOf("int8", keys.toArray()));
      ResultSet result = statement.executeQuery();
      Map<Long, Coordinate> nodes = new HashMap<>();
      while (result.next()) {
        Long id = result.getLong(1);
        Point point = (Point) toGeometry(result.getBytes(2));
        nodes.put(id, point.getCoordinate());
      }
      return keys.stream().map(key -> nodes.get(key)).collect(Collectors.toList());
    } catch (SQLException e) {
      throw new StoreException(e);
    }
  }

}
