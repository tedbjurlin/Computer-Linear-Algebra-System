package AbstractSyntaxTree;

import java.util.*;
import static java.util.Collections.copy;

public class Matrix implements Value {
    List<Scalar> values;
    int row_size;
    int col_size;

    public Matrix(List<Scalar> values, int row_size, int col_size) {
        this.values = values;
        this.row_size = row_size;
        this.col_size = col_size;
    }
    public Matrix(Matrix other) {
        this.values = new ArrayList<Scalar>();
        copy(this.values, other.values);
        this.row_size = other.row_size;
        this.col_size = other.col_size;
    }
    public Matrix(int dim) {
        this.row_size = dim;
        this.col_size = dim;
        this.values = new ArrayList<Scalar>();
        for (int i = 0; i < dim*dim; i++) {
            this.values.add(new Scalar(0));
        }
        for (int i = 0; i < dim; i++) {
            this.values.set(i*dim+i, new Scalar(1));
        }
    }

    public Matrix multiply(Scalar other) {
        Matrix ret = new Matrix(this);
        for (int i = 0; i < ret.col_size*ret.row_size; i++) {
            ret.values.set(i, this.values.get(i).multiply(other));
        }
        return ret;
    }

    public Matrix divide(Scalar other) {
        Matrix ret = new Matrix(this);
        for (int i = 0; i < ret.col_size*ret.row_size; i++) {
            ret.values.set(i, values.get(i).divide(other));
        }
        return ret;
    }

    public Matrix multiply(Matrix other) {
        assert(col_size == other.row_size);

        Matrix ret = new Matrix(new ArrayList<Scalar>(col_size*other.row_size), other.row_size, col_size);
        for (int i = 0; i < col_size*other.row_size; i++) {
            for (int j = 0; j < row_size; j++) {
                ret.values.set(i, values.get((i/other.row_size)*row_size+j).add(other.values.get(i%other.row_size+j*other.row_size)));
            }
        }
        return ret;
    }

    public Matrix add(Matrix other) {
        assert(col_size == other.col_size);
        assert(row_size == other.row_size);

        Matrix ret = new Matrix(this);
        for (int i = 0; i < ret.col_size*ret.row_size; i++) {
            ret.values.set(i, values.get(i).add(other.values.get(i)));
        }
        return ret;
    }

    public Matrix subtract(Matrix other) {
        assert(col_size == other.col_size);
        assert(row_size == other.row_size);

        Matrix ret = new Matrix(this);
        for (int i = 0; i < ret.col_size*ret.row_size; i++) {
            ret.values.set(i, values.get(i).subtract(other.values.get(i)));
        }
        return ret;
    }

    public Matrix augment(Matrix other) {
        assert(col_size == other.col_size);

        Matrix ret = new Matrix(new ArrayList<Scalar>(row_size*col_size + other.row_size*other.col_size), row_size+other.row_size, col_size);
        for (int i = 0; i < ret.row_size*ret.col_size; i++) {
            if (i%col_size - row_size < 0) {
                ret.values.set(i, values.get(i % col_size + (i / (row_size + other.row_size)) * row_size));
            } else {
                ret.values.set(i, other.values.get(i % col_size + (i / (row_size + other.row_size)) * other.row_size - row_size));
            }
        }
        return ret;
    }
}
